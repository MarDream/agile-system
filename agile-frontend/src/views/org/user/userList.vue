<template>
  <div class="comprehensive-table-container">
    <div ref="titleForm">
      <el-row class="vab-query-form">
        <el-col class="top-panel">
          <el-form
            ref="queryForm"
            v-ab-btn-rights:userManager_search
            :inline="true"
            :model="query"
            label-width="62px"
            @submit.prevent
          >
            <el-form-item label="姓名" prop="fullname$VLK">
              <el-input v-model="query.fullname$VLK" placeholder="请输入姓名" />
            </el-form-item>
            <el-form-item label="账户" prop="account$VLK">
              <el-input v-model="query.account$VLK" placeholder="请输入账户" />
            </el-form-item>

            <el-form-item
              v-show="collapse"
              label="状态"
              prop="status$VEQ"
            >
              <el-select
                v-model="query.status$VEQ"
                clearable
                placeholder="全部"
              >
                <el-option label="禁用" value="0" />
                <el-option label="启用" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item v-show="collapse" label="手机" prop="mobile$VLK">
              <el-input v-model="query.mobile$VLK" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item>
              <!--  abTableMix 提供了search,reset,handleCollapse 这些通用方法-->
              <el-button :icon="Search" type="primary" @click="search()">
                查询
              </el-button>
              <el-button :icon="RefreshRight" @click="reset()">重置</el-button>
              <el-button link type="primary" @click="handleCollapse()">
                <span v-if="!collapse" type="primary">展开</span>
                <span v-else type="primary">合并</span>
                <el-icon class="el-icon--right">
                  <ArrowUp v-if="!collapse" />
                  <arrow-down v-else />
                </el-icon>
              </el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col class="left-panel">
          <router-link to="/org/user/userEdit">
            <el-button
              v-ab-btn-rights:userManager_add
              :icon="Plus"
              type="primary"
            >
              添加
            </el-button>
          </router-link>
          <el-button
            v-ab-btn-rights:userManager_batchDel
            :disabled="!selectedData || selectedData.length == 0"
            :icon="Delete"
            style="margin-left: 12px"
            type="danger"
            @click="delBySeletedIds(orgApi.user.OrgUserRemoveUrl)"
          >
            批量删除
          </el-button>
        </el-col>
      </el-row>
    </div>

    <ab-table
      ref="abTable"
      v-model="selectedData"
      :height="tableHeight"
      :query-param="query"
      row-key="id"
      :url="orgApi.user.OrgUserListUrl"
    >
      <ab-column label="姓名" min-width="80" prop="fullname" />
      <ab-column label="账号" min-width="100" prop="account" />
      <ab-column
        ab-effect="plain"
        ab-key="type"
        ab-text="desc"
        ab-type="success"
        label="性别"
        min-width="60"
        prop="sex"
      />
      <ab-column label="邮箱" min-width="100" prop="email" />
      <ab-column label="手机号" min-width="100" prop="mobile" />
      <ab-column
        ab-tag-type="statusCss"
        label="状态"
        min-width="80"
        prop="statusDesc"
      />
      <ab-column label="创建时间" min-width="120" prop="createTime" sortable />
      <ab-column ab-template="edit" fixed="right" label="操作" width="280" />
      <template #edit="{ scope }">
        <router-link :to="{ name: 'userEdit', query: { id: scope.row.id } }">
          <el-button v-ab-btn-rights:userManager_edit text type="primary">
            编辑
          </el-button>
        </router-link>

        <el-button
          v-ab-btn-rights:userManager_updateStatus
          text
          type="primary"
          @click="
            sendAction(
              orgApi.user.OrgUserUpdateStatusUrl + scope.row.id,
              '确定' +
                (scope.row.statusDesc === '启用' ? '禁用' : '启用') +
                scope.row.fullname +
                '吗?'
            )
          "
        >
          {{ scope.row.statusDesc === '启用' ? '禁用' : '启用' }}
        </el-button>
        <el-button
          v-ab-btn-rights:userManager_del
          text
          type="primary"
          @click="
            sendAction(
              orgApi.user.OrgUserRemoveUrl + scope.row.id,
              `确定删除${scope.row.account}吗?`
            )
          "
        >
          删除
        </el-button>
      </template>
    </ab-table>
  </div>
</template>

<script lang="ts">
  import { defineComponent, onMounted, reactive } from 'vue'
  import { abTableMix, orgApi } from 'agilebpm'
  import {
    ArrowDown,
    ArrowUp,
    Delete,
    Edit,
    Plus,
    RefreshRight,
    Search,
  } from '@element-plus/icons-vue'

  export default defineComponent({
    //页面缓存
    name: 'UserList',
    components: { ArrowUp, ArrowDown },
    mixins: [abTableMix],
    setup() {
      const query = reactive({
        fullname$VLK: '',
        account$VLK: '',
        sex$VEQ: '',
        mobile$VLK: '',
        status$VEQ: '',
      })
      // mounted 时机
      onMounted(() => {})

      return {
        query,
        orgApi,
        Delete,
        Edit,
        Search,
        Plus,
        RefreshRight,
      }
    },
  })
</script>
