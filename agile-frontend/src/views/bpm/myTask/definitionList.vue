<template>
  <div class="comprehensive-table-container">
    <el-container>
      <el-main>
        <el-input
          v-model="info.selectVal"
          clearable
          placeholder="快速查找流程"
          size="large"
          style="position: fixed; top: 150px; right: 100px; width: 200px"
          type="text"
          @input="searchFn"
        >
          <template #append>
            <el-button :icon="Search" type="primary" />
          </template>
        </el-input>
        <div v-loading="info.loading" class="myDefinitionList__wrapper">
          <el-empty
            v-if="!info.serachMap || Object.keys(info.serachMap).length === 0"
            description="暂无数据,管理员尚未分配可用流程"
          />
          <div
            v-for="(values, key, index) in info.serachMap"
            v-else
            :key="key"
            class="myDefinitionList__wrapper--group"
          >
            <div class="myDefinitionList__wrapper--group--title">{{ key }}</div>
            <div class="myDefinitionList__wrapper--group--content">
              <div
                v-for="(def, defIndex) in values"
                :key="defIndex"
                class="myDefinitionList__wrapper--group--content--item--info"
              >
                <router-link
                  style="width: 100%"
                  :to="`/bpm/flowStart/${def.key}`"
                >
                  <div style="overflow: hidden; font-size: 14px">
                    <el-row :gutter="5">
                      <el-col :span="4" style="text-align: right">
                        <svg-icon
                          class-name="imag"
                          :icon="def.iconStyle || 'adjustment'"
                          style="width: 28px; height: 28px"
                        />
                      </el-col>
                      <el-col
                        :span="20"
                        style="padding-left: 10px; line-height: 33px"
                      >
                        {{ def.name }}
                      </el-col>
                    </el-row>
                  </div>
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts">
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent, getCurrentInstance } from 'vue'
  import { flowIconColorList } from './components/myTaskConfig'
  import { Search } from '@element-plus/icons-vue'
  import { bpmApi, svgIcon, sysApi } from 'agilebpm'

  export default defineComponent({
    name: 'BpmMyDefinitionList',
    components: { svgIcon },
    setup() {
      const info = reactive({
        //流程定义Map，key：分类名称，value：流程定义列表
        definitionMap: {},
        //搜索结果
        serachMap: {},
        selectVal: '',
        loading: false,
        flowIconColorList: flowIconColorList,
        timer: null as any,
      })
      // @ts-ignore
      const { proxy } = getCurrentInstance()

      const searchFn = (name: string) => {
        // 如果url上有参数typeGroup
        if (
          proxy.$route.query.typeGroup &&
          proxy.$route.query.typeGroup.length > 0
        ) {
          return
        }

        if (info.timer) {
          clearTimeout(info.timer)
        }

        if (!name) {
          info.serachMap = info.definitionMap
          return
        }

        info.timer = setTimeout(() => {
          const result = {}
          for (const key in info.definitionMap) {
            const values = info.definitionMap[key]
            if (values && values.length > 0) {
              const valuesTemp: any = []
              for (let i = 0; i < values.length; i++) {
                if (values[i].name.includes(name)) {
                  valuesTemp.push(values[i])
                }
              }
              if (valuesTemp.length > 0) {
                result[key] = valuesTemp
              }
            }
          }
          info.serachMap = result
        }, 800)
      }

      //请求常用流程列表
      const getData = async () => {
        const bpmRes: any = await bpmApi.myTask.bpmMydefinitionListUrl({})
        const definitList: any[] = bpmRes.data
        if (!definitList || definitList.length === 0) {
          return
        }

        const typeList: any = await sysApi.dict.getDictListByCode('flowType')
        if (!typeList || !typeList.length || typeList.length === 0) {
          info.definitionMap['未知分类'] = definitList
          info.serachMap = info.definitionMap
          return
        }

        //对 bpmDefinitions 按typeCode 进行分组,找出没有分类的流程 放到未知分类中
        const noTypeDefinitions = [] as any[]
        const bpmDefinitionMap = {} as any
        definitList.forEach((bpmItem: any) => {
          const node: any = typeList.find((item: any) => {
            return item.code === bpmItem.typeCode
          })
          if (!bpmItem.typeCode || !node) {
            noTypeDefinitions.push(bpmItem)
          } else {
            if (!bpmDefinitionMap[bpmItem.typeCode]) {
              bpmDefinitionMap[bpmItem.typeCode] = [] as any[]
            }
            bpmDefinitionMap[bpmItem.typeCode].push(bpmItem)
          }
        })
        //对流程进行分组
        const resultCode = {} as any
        const resultMap = {} as any
        typeList.forEach((dictNode: any) => {
          const definitions = bpmDefinitionMap[dictNode.code]
          if (definitions && definitions.length > 0) {
            if (Object.keys(resultCode).includes(dictNode.code)) {
              definitions.forEach((item: any) => {
                resultMap[resultCode[dictNode.code]].push(item)
              })
            } else {
              resultCode[dictNode.code] = dictNode.name
              resultMap[dictNode.name] = definitions
            }
          }
        })

        if (noTypeDefinitions.length > 0) {
          resultMap['未知分类'] = noTypeDefinitions
        }

        info.definitionMap = resultMap
        info.serachMap = info.definitionMap

        // 如果url上有参数typeGroup
        if (
          proxy.$route.query.typeGroup &&
          proxy.$route.query.typeGroup.length > 0
        ) {
          const name = typeList.filter(
            (item: any) => item.code == proxy.$route.query.typeGroup
          )[0].name
          const obj = {}
          obj[name] = info.serachMap[name]
          info.serachMap = obj
        }
      }

      onMounted(() => {
        getData()
      })

      return {
        info,
        svgIcon,
        Search,
        searchFn,
        bpmApi,
      }
    },
  })
</script>
<style lang="scss" scoped>
  .myDefinitionList__wrapper {
    /*     height: calc(100% - 10px);
    padding: 24px !important; */
    margin: 5px;
    background: #ffffff;
    border-radius: 4px;
    &--group {
      margin-top: 10px;
      &--title {
        position: relative;
        height: 20px;
        padding-left: 6px;
        font-family: PingFangSC-Semibold;
        font-size: 14px;
        font-weight: 600;
        color: #323233;
        &::before {
          position: absolute;
          top: 5px;
          left: 0px;
          display: block;
          width: 2px;
          height: 10px;
          content: '';
          background: #d6000f;
        }
      }
      &--content {
        display: flex;
        flex-wrap: wrap;
        align-items: center;
        padding: 16px 0px;
        &--item {
          width: 16%;
          margin-right: 10px;
          margin-bottom: 10px;
          &--info {
            display: flex;
            align-items: center;
            width: 200px;
            height: 62px;
            padding-left: 22px;
            &:hover {
              background: #ffffff;
              border-radius: 8px;
              box-shadow: 0px 4px 20px 0px rgba(232, 232, 235, 1);
            }
            img {
              width: 30px;
              height: 30px;
            }
            span {
              display: block;
              max-width: 150px;
              padding-left: 6px;
              font-family: PingFangSC-Regular;
              font-size: 14px;
              font-weight: 400;
              color: #323233;
              white-space: pre-wrap;
            }
          }
        }
      }
    }
    #searchBox {
      position: fixed;
      top: 133px;
      right: 20px;
      float: right;
      width: 200px;
    }
  }
</style>
