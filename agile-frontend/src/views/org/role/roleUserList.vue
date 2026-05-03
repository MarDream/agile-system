<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <h3>角色【{{ roleName }}】用户列表</h3>
        <el-divider />
      </el-row>
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            :inline="true"
            label-width="62px"
            :model="query"
            @submit.prevent
          >
            <el-form-item
              label="姓名"
              prop="['tuser.fullname$VLK']"
            >
              <el-input
                v-model="query['tuser.fullname$VLK']"
                placeholder="请输入姓名"
              />
            </el-form-item>
            <el-form-item
              label="账户"
              prop="['tuser.account$VLK']"
            >
              <el-input
                v-model="query['tuser.account$VLK']"
                placeholder="请输入账户"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                v-ab-btn-rights:orgRelation_roleJson
                :icon="Search"
                type="primary"
                @click="search()"
              >
                查询
              </el-button>
              <el-button
                v-ab-btn-rights:orgRelation_roleJson
                :icon="RefreshRight"
                @click="reset()"
              >
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col class="left-panel">
          <el-space>
            <el-button :icon="Back" type="primary" @click="back">
              返回
            </el-button>
            <ab-cust-dialog
              dialog-key="userSelector"
              :icon="Plus"
              type="primary"
              @ok="userSelectorOk($event, search)"
            >
              添加用户
            </ab-cust-dialog>
            <el-button
              v-ab-btn-rights:orgRelation_batchDel
              :disabled="!selectedData || selectedData.length == 0"
              :icon="Delete"
              type="danger"
              @click="delBySeletedIds(orgApi.post.orgRelationRemove)"
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
      :url="orgApi.post.roleUserList"
    >
      <ab-column label="姓名" min-width="120" prop="userFullname" />
      <ab-column label="账户" min-width="120" prop="userAccount" />
      <ab-column label="角色名称" min-width="150" prop="roleName" />
      <ab-column ab-template="groupName" label="隶属部门" min-idth="130" />
      <template #groupName="{ scope }">
        {{ scope.row.groupName ?? '-' }}
      </template>
      <ab-column
        ab-date-formatter="yyyy-MM-dd HH:mm"
        label="创建时间"
        prop="createTime"
        sortable
        width="170"
      />
      <ab-column ab-template="edit" fixed="right" label="操作" width="120" />
      <template #edit="{ scope }">
        <el-button
          v-ab-btn-rights:orgRelation_del
          text
          type="primary"
          @click="
            sendAction(
              orgApi.post.orgRelationRemove + scope.row.id,
              `确定移除【${scope.row.userFullname}】吗?`
            )
          "
        >
          移除
        </el-button>
      </template>
    </ab-table>
  </div>
</template>

<script lang="ts">
  import { abTableMix, orgApi, abUtil, abTools, getData } from 'agilebpm'
  export default { mixins: [abTableMix] }
</script>

<script lang="ts" setup>
  import { reactive } from 'vue'
  import { ElMessage } from 'element-plus'
  import {
    Search,
    Delete,
    Plus,
    RefreshRight,
    Back,
  } from '@element-plus/icons-vue'
  import { useAbStoreAdapter } from '@/store/modules/abStoreAdapter'

  const { closeTab } = useAbStoreAdapter()
  const route = useRoute()

  const { proxy } = abTools.useCurrentInstance()
  const name = 'RoleUserList'
  const roleId = proxy.$route.query.roleId
  const roleName = ref(proxy.$route.params.roleName)

  // 如果角色名称不存在，从后端获取
  if (!roleName.value) {
    getData(orgApi.role.orgRoleGetUrl, { id: roleId }).then(
      (res) => (roleName.value = res.data.name)
    )
  }

  // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
  const query: any = reactive({
    roleId: proxy.$route.query.roleId,
  })

  const userSelectorOk = (dataList: [any], searchFn: any) => {
    if (!dataList || !dataList.length) {
      return
    }
    orgApi.post
      .saveRoleUsers(roleId, dataList.map((item) => item.id) as [string])
      .then((res) => {
        ElMessage.success(res.data)
        searchFn()
      })
  }

  const back = () => {
    closeTab(route.path)
    proxy?.$router.push({
      name: 'RoleList',
    })
  }
</script>
