<template>
  <div class="common-layout">
    <el-container>
      <el-aside v-if="!info.typeGroup" class="left-tree">
        <flow-type-tree
          v-model="randomModel"
          :tree-url="bpmApi.myTask.todoListTypeTreeUrl"
          @node-click="searchData"
        />
      </el-aside>
      <el-main>
        <div class="comprehensive-table-container">
          <div ref="titleForm">
            <el-row class="vab-query-form">
              <el-col class="top-panel">
                <el-form
                  ref="queryForm"
                  :inline="true"
                  label-width="90px"
                  :model="query"
                  @submit.prevent
                >
                  <el-form-item label="任务标题" prop="title$VLK">
                    <el-input
                      v-model="query.title$VLK"
                      placeholder="请输入标题"
                    />
                  </el-form-item>
                  <el-form-item label="任务名称" prop="task.name$VLK">
                    <el-input
                      v-model="query['task.name$VLK']"
                      placeholder="请输入名称"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="search()">
                      查询
                    </el-button>
                    <el-button :icon="RefreshRight" @click="reset()">
                      重置
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-col>
            </el-row>
          </div>

          <ab-table
            ref="abTable"
            v-model="selectedData"
            :checkable="false"
            :height="tableHeight"
            :query-param="query"
            :url="bpmApi.myTask.bpmMyTaskTodoListUrl"
          >
            <ab-column label="任务标题" min-width="120" prop="title" />
            <ab-column label="任务名称" prop="name" width="120" />
            <ab-column
              ab-tag-type="statusCss"
              label="状态"
              prop="statusDesc"
              width="80"
            />
            <ab-column
              ab-tag-type="typeCss"
              label="类型"
              prop="typeDesc"
              width="95"
            />
            <ab-column ab-template="priority" label="紧急程度" width="85" />
            <template #priority="{ scope }">
              <el-tag
                :color="custFormat(scope.row.priority)?.color"
                effect="light"
                style="color: aliceblue"
              >
                {{ custFormat(scope.row.priority)?.name }}
              </el-tag>
            </template>
            <ab-column label="任务创建时间" prop="createTime" width="160" />
            <ab-column ab-template="durMs" label="任务持续时间" width="110" />
            <template #durMs="{ scope }">
              {{ abUtil.timeLag(scope.row.durMs, 2) }}
            </template>
            <ab-column ab-template="dueTime" label="任务到期时间" width="160" />
            <template #dueTime="{ scope }">
              <span
                v-if="new Date(scope.row.dueTime) < new Date()"
                style="color: crimson"
              >
                {{ scope.row.dueTime }}
              </span>
              <span v-else>{{ scope.row.dueTime }}</span>
            </template>
            <ab-column
              ab-template="edit"
              fixed="right"
              label="操作"
              prop="key"
              width="85"
            />
            <template #edit="{ scope }">
              <router-link
                :to="{
                  name: 'BpmTaskComplate',
                  query: {
                    id: scope.row.id,
                    type: 'taskId',
                    backR: 'BpmMyTaskTodoList',
                  },
                }"
              >
                <el-button text type="primary">办理</el-button>
              </router-link>
            </template>
          </ab-table>
        </div>
      </el-main>
    </el-container>
  </div>
</template>
<script lang="ts">
  import { reactive, defineComponent, getCurrentInstance } from 'vue'
  import { abTableMix, bpmApi, abUtil } from 'agilebpm'
  import flowTypeTree from './components/flow-type-tree.vue'
  import {
    Delete,
    Search,
    RefreshRight,
    InfoFilled,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'BpmMyTaskTodoList',
    components: { flowTypeTree },
    mixins: [abTableMix],
    setup() {
      // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
      const query: any = reactive({
        'task.name$VLK': '',
        'def.typeCode$VIN': '',
        'def.key$VEQ': '',
        title$VLK: '',
      })

      const randomModel = ref('')
      // @ts-ignore
      const { proxy } = getCurrentInstance()
      const info: any = reactive({
        typeGroup: false,
      })
      if (
        proxy.$route.query.typeGroup &&
        proxy.$route.query.typeGroup.length > 0
      ) {
        info.typeGroup = true
        query['def.typeCode$VIN'] = proxy.$route.query.typeGroup
      }

      const custFormat = (value: number) => {
        if (!value) return
        if (value < 51) {
          return {
            name: '正常',
            type: '',
            color: '#409EFF',
          }
        }
        if (value == 51) {
          return { name: '优先', type: 'success', color: '#ffadd2' }
        }
        if (value == 52) {
          return { name: '加急', type: 'info', color: '#E6A23C' }
        }
        if (value == 53) {
          return { name: '紧急', type: 'warning', color: '#f90' }
        }
        if (value == 54) {
          return { name: '特急', type: 'warning', color: '#ed4014' }
        }
        if (value >= 55) {
          return { name: '火急', type: 'danger', color: '#ed4014' }
        }
      }

      const search = () => {
        // 随机数改变 重新获取树
        randomModel.value = `${Math.floor(Math.random() * 100)}`
        proxy.$refs['abTable'].getData()
      }

      const searchData = (type: string, value: string) => {
        if (type === 'default') {
          query['def.typeCode$VIN'] = null
          query['def.key$VEQ'] = null
          proxy.$refs['abTable'].getData()
          return
        }
        if (type === 'flow') {
          query['def.typeCode$VIN'] = null
          query['def.key$VEQ'] = value
          proxy.$refs['abTable'].getData()
          return
        }
        if (type === 'tree') {
          query['def.key$VEQ'] = null
          query['def.typeCode$VIN'] = value
          proxy.$refs['abTable'].getData()
        }
      }

      return {
        custFormat,
        searchData,
        query,
        info,
        bpmApi,
        Search,
        RefreshRight,
        Delete,
        InfoFilled,
        abUtil,
        search,
        randomModel,
      }
    },
  })
</script>
