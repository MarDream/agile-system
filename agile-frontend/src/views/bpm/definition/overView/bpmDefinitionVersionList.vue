<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-space>
        <el-button :icon="Back" type="primary" @click="back">返回</el-button>
        <!-- <el-button
          :disabled="!selectedData || selectedData.length == 0"
          :icon="Download"
          type="primary"
          @click="exportFn"
        >
          导出
        </el-button> -->
      </el-space>
    </div>

    <!-- 关键字段设置minwith，特定枚举字段设置with（管理列、日期、状态、字典等），
         不重要字段放最后，管理列设置 fixed="right"
    -->
    <ab-table
      ref="abTable"
      v-model="selectedData"
      :height="tableHeight"
      :query-param="query"
      row-key="id"
      :url="bpmApi.bpmDefinition.bpmDefinitionListUrl"
    >
      <ab-column label="流程名称" min-width="120" prop="name" />
      <ab-column label="流程编码" min-width="100" prop="key" />
      <ab-column label="描述" min-width="150" prop="desc" />
      <ab-column
        ab-text-formatter="1-支持-success|0-不支持-"
        label="移动端"
        prop="supportMobile"
        width="90"
      />
      <ab-column
        ab-text-formatter="deploy-发布-success-dark|forbidden-禁用-danger-dark|draft-草稿-info"
        label="状态"
        prop="status"
        width="80"
      />
      <ab-column label="版本" prop="version" width="55" />
      <ab-column
        ab-text-formatter="1-是-success-dark|0-否-info-dark"
        label="主版本"
        prop="isMain"
        width="80"
      >
        主版本
      </ab-column>
      <ab-column label="排序" prop="sn" width="55" />
      <ab-column
        ab-date-formatter="yyyy-MM-dd"
        label="更新时间"
        prop="updateTime"
        width="120"
      />
      <ab-column ab-template="edit" fixed="right" label="操作" width="315" />
      <template #edit="{ scope }">
        <el-button
          v-show="scope.row.processEditor !== 'sim'"
          v-ab-btn-rights:bpmDefinition_preview
          text
          type="primary"
          @click="openOverView(scope.row)"
        >
          配置预览
        </el-button>

        <el-button text type="primary" @click="showDesign(scope.row)">
          设计
        </el-button>
        <router-link :to="`/bpm/flowStart/${scope.row.key}`">
          <el-button text type="primary">启动</el-button>
        </router-link>
        <el-button
          v-if="scope.row.isMain === 0"
          v-show="scope.row.processEditor !== 'sim'"
          v-ab-btn-rights:bpmDefinition_setMain
          text
          type="primary"
          @click="
            sendAction(
              bpmApi.bpmDefinition.bpmDefinitionSetMainUrl + scope.row.id,
              `确定把版本 ${scope.row.version} 设为主版本吗?`
            )
          "
        >
          设为主版本
        </el-button>
        <el-button
          v-if="scope.row.isMain === 0"
          v-show="scope.row.processEditor === 'sim'"
          v-ab-btn-rights:simDefinition_setMain
          text
          type="primary"
          @click="
            sendAction(
              bpmApi.bpmDefinition.bpmDefinitionSetMainUrl + scope.row.id,
              `确定把版本 ${scope.row.version} 设为主版本吗?`
            )
          "
        >
          设为主版本
        </el-button>
      </template>
    </ab-table>
    <bpm-definition-overall-view
      v-model="overViewInfo.isShow"
      :def-id="overViewInfo.defId"
      :title="overViewInfo.title"
    />
    <ab-designer
      v-if="designInfo.show"
      v-model="designInfo.show"
      :def-id="designInfo.defId"
      @close="closeDefinition()"
    />
  </div>
</template>

<script lang="ts">
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent, getCurrentInstance } from 'vue'
  import { abTableMix, bpmApi, abTools } from 'agilebpm'
  import { Back, Download } from '@element-plus/icons-vue'
  import { useAbStoreAdapter } from '@/store/modules/abStoreAdapter'
  // import abDesigner from 'abDesigner'

  export default defineComponent({
    name: 'BpmDefinitionVersionList',
    components: {  },
    mixins: [abTableMix],
    setup() {
      // @ts-ignore
      const { proxy } = getCurrentInstance()
      const defKey = proxy.$route.query.key

      console.log(proxy.$router.getRoutes())

      // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
      const query = reactive({
        name$VLK: '',
        key$VEQ: defKey,
        typeCode$VEQ: '',
        isVersions: true,
      })
      const designInfo = reactive({
        show: false,
        defId: '',
      })
      const { delVisitedRoute } = useAbStoreAdapter()
      const back = () => {
        proxy.$router ? proxy.$router.back() : window.history.back()
      }
      const overViewInfo = reactive({
        isShow: false,
        title: '',
        defId: '',
      })
      const openOverView = (bemDefiniton: any) => {
        overViewInfo.isShow = true
        overViewInfo.title = bemDefiniton.name
        overViewInfo.defId = bemDefiniton.id
      }
      //导出
      const exportFn = () => {
        const defIds = proxy.selectedData.map((row: any) => row.id).join(',')
        bpmApi.bpmDefinition.exportBpmDef(defIds, exportCallback)
      }
      //导出回调，刷新页面，去掉选中状态
      const exportCallback = () => {
        proxy.selectedData.splice(0, proxy.selectedData.length)
        proxy.search()
      }
      const showDesign = (row: any) => {
        if (row.processEditor == 'sim') {
          window.open(`/simpleWF?id=${row.id}&mode=diyBo`)
        } else {
          window.open(
            abTools.getResolveUrl(`/bpmDesigner?defId=${row.id}`, proxy)
          )
        }
      }
      const closeDefinition = () => {
        designInfo.show = false
        designInfo.defId = ''
        proxy.search()
      }
      return {
        query,
        bpmApi,
        Back,
        Download,
        back,
        openOverView,
        overViewInfo,
        showDesign,
        closeDefinition,
        exportFn,
        designInfo,
      }
    },
  })
</script>
