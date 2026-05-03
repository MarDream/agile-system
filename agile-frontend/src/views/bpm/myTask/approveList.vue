<template>
  <div class="common-layout">
    <el-container>
      <el-aside v-if="!info.typeGroup" class="left-tree">
        <flow-type-tree
          :tree-url="bpmApi.myTask.approveListTypeTreeUrl"
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
                  label-width="62px"
                  :model="query"
                  @submit.prevent
                >
                  <el-form-item label="标题" prop="title$VLK">
                    <el-input
                      v-model="query.title$VLK"
                      placeholder="请输入标题"
                    />
                  </el-form-item>
                  <el-form-item label="流程" prop="name$VLK">
                    <el-input
                      v-model="query.name$VLK"
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
            row-key="id"
            :url="bpmApi.myTask.bpmMyApproveListUrl"
          >
            <ab-column label="标题" min-width="120" prop="title" />
            <ab-column label="流程" min-width="80" prop="defName" />
            <ab-column label="节点" min-width="60" prop="nodeName" />
            <ab-column label="发起人" prop="creator" width="120" />
            <ab-column label="办理时间" prop="approveTime" width="160" />
            <ab-column ab-template="durationMs" label="处理用时" width="90" />
            <template #durationMs="{ scope }">
              {{ abUtil.timeLag(scope.row.durationMs, 2) }}
            </template>
            <ab-column
              ab-tag-type="statusCss"
              label="实例状态"
              prop="statusDesc"
              width="95"
            />
            <ab-column
              ab-tag-type="approveStatusLabelCss"
              label="处理结果"
              prop="approveStatusValue"
              width="95"
            />
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
                  name: 'BpmInstanceDetail',
                  query: {
                    id: scope.row.id,
                    type: 'opId',
                  },
                }"
              >
                <el-button text type="primary">详情</el-button>
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
    Edit,
    Search,
    Plus,
    RefreshRight,
    InfoFilled,
  } from '@element-plus/icons-vue'
  import PendingApprovalTaskPopover from './components/pending-approval-task-popover.vue'

  export default defineComponent({
    name: 'BpmMyApproveList',
    components: { PendingApprovalTaskPopover, flowTypeTree },
    mixins: [abTableMix],
    setup() {
      // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
      const query: any = reactive({
        defName$VLK: '',
        'def.typeCode$VIN': '',
        defKey$VEQ: '',
        title$VLK: '',
      })
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
      const searchData = (type: string, value: string) => {
        if (type === 'default') {
          query['def.typeCode$VIN'] = null
          query['def.key$VEQ'] = null
        }
        if (type === 'flow') {
          query['def.typeCode$VIN'] = null
          query.defKey$VEQ = value
        } else if (type === 'tree') {
          query['def.typeCode$VIN'] = value
          query.defKey$VEQ = null
        }
        proxy.search()
      }
      return {
        query,
        info,
        bpmApi,
        abUtil,
        searchData,
        Search,
        Plus,
        RefreshRight,
        Delete,
        Edit,
        InfoFilled,
      }
    },
  })
</script>
