<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <flow-type-tree
          :tree-url="bpmApi.myTask.instanceListTypeTreeUrl"
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
                  <el-form-item label="流程编码" prop="defKey$VLK">
                    <el-input
                      v-model="query.defKey$VLK"
                      placeholder="请输入流程编码"
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
              <el-col class="left-panel">
                <el-button
                  v-ab-btn-rights:instance_del
                  :disabled="!selectedData || selectedData.length == 0"
                  :icon="Delete"
                  type="danger"
                  @click="delBySeletedIds(bpmApi.instance.bpmInstanceDeleteUrl)"
                >
                  批量删除
                </el-button>
              </el-col>
            </el-row>
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
            :url="bpmApi.instance.bpmInstanceListUrl"
          >
            <ab-column label="流程标题" min-width="250" prop="title" />
            <ab-column label="流程编码" min-width="120" prop="defKey" />
            <ab-column
              ab-text-formatter="0-否-success-dark|1-是-danger-dark"
              label="挂起"
              prop="suspensionState"
              width="60"
            />
            <ab-column
              ab-tag-type="statusCss"
              label="状态"
              prop="statusDesc"
              width="95"
            />
            <ab-column label="创建时间" prop="createTime" width="160" />
            <ab-column label="创建人" prop="creator" width="120" />
            <ab-column label="完成时间" prop="endTime" width="160" />
            <ab-column ab-template="durMs" label="持续时间" width="90" />
            <template #durMs="{ scope }">
              {{ abUtil.timeLag(scope.row.durationMs, 2) }}
            </template>
            <ab-column label="更新时间" prop="updateTime" width="160" />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="管理"
              width="290"
            />
            <template #edit="{ scope }">
              <router-link
                :to="{
                  name: 'BpmInstanceListDetail',
                  query: { id: scope.row.id },
                }"
              >
                <el-button text type="primary">详情</el-button>
              </router-link>
              <el-button
                text
                type="primary"
                @click="
                  sendAction(
                    bpmApi.instance.bpmInstanceDeleteUrl + scope.row.id,
                    `确定删除 ${scope.row.title} 吗?`
                  )
                "
              >
                删除
              </el-button>
              <el-button
                text
                type="primary"
                @click="openImageDialog(scope.row.id)"
              >
                流程图
              </el-button>
              <el-button
                v-if="
                  scope.row.suspensionState === 0 &&
                  scope.row.status != 'completed' &&
                  scope.row.status != 'draft' &&
                  scope.row.status != 'manualEnd' &&
                  scope.row.status != 'revoke_end'
                "
                text
                type="primary"
                @click="forbidden(scope.row)"
              >
                挂起
              </el-button>
              <el-button
                v-if="scope.row.suspensionState === 1"
                text
                type="primary"
                @click="forbidden(scope.row)"
              >
                取消挂起
              </el-button>
            </template>
          </ab-table>
          <bpm-image-dialog
            v-if="flowImageInfo.showImageDialog"
            :bpm-data="flowImageInfo.bpmData"
            :is-show="flowImageInfo.showImageDialog"
            title="流程图"
            @dialog-cancel="dialogCancel"
          />
        </div>
      </el-main>
    </el-container>
  </div>
</template>
<script lang="ts">
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent, getCurrentInstance } from 'vue'
  import { abTableMix, bpmApi, abUtil, BpmImageDialog } from 'agilebpm'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import flowTypeTree from '../myTask/components/flow-type-tree.vue'
  import {
    Delete,
    Edit,
    Search,
    Plus,
    RefreshRight,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'BpmInstanceList',
    components: { BpmImageDialog, flowTypeTree },
    mixins: [abTableMix],

    setup() {
      // @ts-ignore
      const { proxy } = getCurrentInstance()
      const query = reactive({
        title$VLK: '',
        defKey$VLK: '',
        typeCode$VIN: '',
        defKey$VEQ: '',
      })
      const flowImageInfo = reactive({
        showImageDialog: false,
        bpmData: {
          actionData: {
            instanceId: '',
          },
        } as BpmData,
      })

      const openImageDialog = (instaceId: string) => {
        flowImageInfo.showImageDialog = true
        flowImageInfo.bpmData.actionData.instanceId = instaceId
      }

      const dialogCancel = (isShow: boolean) => {
        flowImageInfo.showImageDialog = false
      }

      const forbidden = (row: any) => {
        ElMessageBox.confirm(
          row.suspensionState == 1
            ? `确定要取消挂起流程${row.title}吗`
            : `确定要挂起流程${row.title}吗`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        ).then(async () => {
          const forbiddenFlag = row.suspensionState == 1 ? false : true
          await bpmApi.instance.toForbidden(row.id, forbiddenFlag).then(
            (result: any) => {
              proxy.search()
              ElMessage({
                type: 'success',
                message: result.msg || '操作成功',
                onClose: () => {},
              })
            },
            () => {}
          )
        })
      }

      const searchData = (type: string, value: string) => {
        if (type === 'default') {
          query.defKey$VEQ = ''
          query.typeCode$VIN = ''
        }
        if (type === 'flow') {
          query.defKey$VEQ = value
          query.typeCode$VIN = ''
        } else if (type === 'tree') {
          query.typeCode$VIN = value
          query.defKey$VEQ = ''
        }
        proxy.search()
      }

      return {
        query,
        bpmApi,
        flowImageInfo,
        openImageDialog,
        dialogCancel,
        forbidden,
        Delete,
        Edit,
        Search,
        Plus,
        RefreshRight,
        abUtil,
        searchData,
      }
    },
  })
</script>
