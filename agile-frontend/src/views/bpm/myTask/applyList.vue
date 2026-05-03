<template>
  <div class="common-layout">
    <el-container>
      <el-aside v-if="!info.typeGroup" class="left-tree">
        <flow-type-tree
          :tree-url="bpmApi.myTask.applyListTypeTreeUrl"
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
                  <el-form-item label="流程标题" prop="title$VLK">
                    <el-input
                      v-model="query.title$VLK"
                      placeholder="请输入流程标题"
                    />
                  </el-form-item>
                  <el-form-item label="流程状态" prop="inst.status$VEQ">
                    <el-select
                      v-model="query['inst.status$VEQ']"
                      placeholder="请输状态"
                    >
                      <el-option
                        v-for="(item, index) in statusList"
                        :key="index"
                        :label="item.value"
                        :value="item.key"
                      />
                    </el-select>
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
          <!-- 关键字段设置minwith，特定枚举字段设置with（管理列、日期、状态、字典等），
         不重要字段放最后，管理列设置 fixed="right"
    -->
          <ab-table
            ref="abTable"
            v-model="selectedData"
            :checkable="false"
            :height="tableHeight"
            :query-param="query"
            row-key="id"
            :url="bpmApi.myTask.bpmMyApplyTaskListUrl"
          >
            <ab-column label="流程" min-width="120" prop="defName" />
            <ab-column label="流程标题" min-width="300" prop="title" />
            <ab-column
              ab-tag-type="statusCss"
              label="状态"
              prop="statusDesc"
              width="95"
            />
            <ab-column label="创建时间" prop="createTime" width="180" />
            <ab-column label="完成时间" prop="endTime" width="160" />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="管理"
              width="85"
            />
            <template #edit="{ scope }">
              <router-link
                :to="{
                  name: 'BpmInstanceDetail',
                  query: {
                    id: scope.row.id,
                    type: 'instIdStart',
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
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent, onMounted, getCurrentInstance } from 'vue'
  import { abTableMix, bpmApi, abUtil, sysApi } from 'agilebpm'
  import flowTypeTree from './components/flow-type-tree.vue'
  import { Search, Plus, RefreshRight } from '@element-plus/icons-vue'
  import PendingApprovalTaskPopover from './components/pending-approval-task-popover.vue'

  export default defineComponent({
    name: 'BpmMyApplyList',
    components: { PendingApprovalTaskPopover, flowTypeTree },
    mixins: [abTableMix],
    setup() {
      const query: any = reactive({
        title$VLK: '',
        defKey$VEQ: '',
        'def.typeCode$VIN': '',
        'inst.status$VEQ': '',
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
        query['def.typeCode$VEQ'] = proxy.$route.query.typeGroup
      }
      const statusList = ref([])
      onMounted(() => {
        sysApi.tools
          .getEnum('com.dstz.bpm.api.enums.InstanceStatus')
          .then((result: any) => {
            statusList.value = result.data
          })
      })
      const searchData = (type: string, value: string) => {
        if (type === 'default') {
          query.defKey$VEQ = null
          query['def.typeCode$VIN'] = null
        }
        if (type === 'flow') {
          query.defKey$VEQ = value
          query['def.typeCode$VIN'] = null
        } else if (type === 'tree') {
          query['def.typeCode$VIN'] = value
          query.defKey$VEQ = null
        }
        proxy.search()
      }

      query['inst.status$VEQ'] = proxy.$route.query.status

      return {
        query,
        info,
        bpmApi,
        statusList,
        searchData,
        abUtil,
        Search,
        Plus,
        RefreshRight,
      }
    },
  })
</script>
