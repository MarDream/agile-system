<template>
  <div ref="titleForm">
    <el-row class="vab-query-form">
      <el-col class="top-panel">
        <el-form
          ref="queryForm"
          v-ab-btn-rights:groupUser_search
          :inline="true"
          label-width="62px"
          :model="query"
        >
          <el-form-item label="姓名" prop="fullname$VLK">
            <el-input
              v-model="query.fullname$VLK"
              clearable
              placeholder="请输入姓名"
              type="text"
            />
          </el-form-item>

          <el-form-item label="账户" prop="account$VLK">
            <el-input
              v-model="query.account$VLK"
              clearable
              placeholder="请输入账户"
              type="text"
            />
          </el-form-item>
          <el-form-item>
            <el-button :icon="Search" type="primary" @click="search()">
              查询
            </el-button>
            <el-button :icon="RefreshRight" @click="reset()">重置</el-button>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col class="left-panel">
        <el-space>
          <el-button
            v-ab-btn-rights:orgRelation_addGroupUser
            :icon="Plus"
            type="primary"
            @click="info.showDialog = true"
          >
            新增岗位用户
          </el-button>
          <el-button
            v-ab-btn-rights:groupManager_batchDel
            :disabled="!selectedData || selectedData.length == 0"
            :icon="Delete"
            type="danger"
            @click="delBySeletedIds(orgApi.post.removeGroupUserUrl)"
          >
            批量删除
          </el-button>
        </el-space>
      </el-col>
    </el-row>
  </div>

  <ab-table
    ref="abTable"
    v-model="selectedData"
    :height="tableHeight"
    :query-param="query"
    row-key="id"
    :url="orgApi.post.queryGroupUserUrl"
  >
    <ab-column label="姓名" min-width="100" prop="userFullname" />

    <ab-column label="账号" min-width="100" prop="userAccount" />

    <ab-column label="组织" min-width="130" prop="groupName" />

    <ab-column label="岗位名称" min-width="130" prop="roleName" />

    <ab-column
      ab-tag-type="isMasterCss"
      label="主组织"
      min-width="80"
      prop="isMasterDesc"
    />

    <ab-column
      ab-tag-type="statusCss"
      label="状态"
      min-width="80"
      prop="statusDesc"
    />

    <ab-column
      date-format="yyyy-MM-dd"
      label="创建时间"
      min-width="150"
      prop="createTime"
      sortable
    />

    <ab-column
      ab-template="edit"
      fixed="right"
      label="管理"
      temp="action"
      width="300"
    />
    <template #edit="{ scope }">
      <el-button
        v-ab-btn-rights:orgRelation_updateStatus
        text
        type="primary"
        @click="
          sendAction(
            orgApi.post.updateGroupUserStatusUrl + scope.row.id,
            '确定' +
              (scope.row.statusDesc === '启用' ? '禁用' : '启用') +
              scope.row.userFullname +
              '吗?'
          )
        "
      >
        {{ scope.row.statusDesc === '启用' ? '禁用' : '启用' }}
      </el-button>

      <el-button
        v-ab-btn-rights:orgRelation_setMaster
        text
        type="primary"
        @click="
          sendAction(
            orgApi.post.updateGroupUserMasterUrl + scope.row.id,
            '确定' +
              (scope.row.isMasterDesc === '是' ? '取消主组织' : '设为主组织') +
              '吗?'
          )
        "
      >
        {{ scope.row.isMasterDesc === '是' ? '取消主组织' : '设为主组织' }}
      </el-button>
      <el-button
        v-ab-btn-rights:orgRelation_remove
        text
        type="primary"
        @click="
          sendAction(
            orgApi.post.removeGroupUserUrl + scope.row.id,
            `确定删除${scope.row.userFullname}吗?`
          )
        "
      >
        删除
      </el-button>
    </template>
  </ab-table>

  <addGroupUser
    :current-org="currentOrg"
    name="addGroupRoleUser"
    :show-dialog="info.showDialog"
    @close="closeSaveDialog"
    @reSearch="reSearchFn"
  />
</template>

<script lang="ts">
  import { reactive, defineComponent } from 'vue'
  import { abTableMix, orgApi } from 'agilebpm'
  import addGroupUser from '@/views/org/group/addGroupUser.vue'
  import { Delete, Plus, RefreshRight, Search } from '@element-plus/icons-vue'

  export default defineComponent({
    name: 'GroupUserList',
    components: { addGroupUser },
    mixins: [abTableMix],
    props: {
      orgId: {
        type: String,
        required: true,
      },
      currentOrg: {
        type: Object,
        required: true,
      },
    },
    setup(props) {
      // @ts-ignore
      const { proxy } = getCurrentInstance()

      const query: any = reactive({
        groupId: props.orgId,
      })

      const info: any = reactive({
        showDialog: false,
        selectedData: [],
      })

      const reSearchFn = () => {
        proxy.search()
      }

      const closeSaveDialog = (isAdd: any) => {
        info.showDialog = false
        if (isAdd) {
          // search();
        }
      }
      watch(
        () => props.orgId,
        (orgId) => {
          if (orgId) {
            query.groupId = orgId
            proxy.search()
          }
        }
      )
      onMounted(() => {
        query.groupId = props.orgId
      })
      return {
        query,
        info,
        reSearchFn,
        closeSaveDialog,
        orgApi,
        Delete,
        Plus,
        RefreshRight,
        Search,
      }
    },
  })
</script>
