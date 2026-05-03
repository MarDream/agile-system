<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <ab-dict-tree
          v-model="query.typeCode$VEQ"
          type-code="jsfl"
          @node-click="searchResetPage()"
        />
      </el-aside>
      <el-main>
        <div class="comprehensive-table-container">
          <div ref="titleForm">
            <el-row class="vab-query-form">
              <el-col class="top-panel">
                <el-form
                  ref="queryForm"
                  v-ab-btn-rights:roleManager_search
                  :inline="true"
                  :model="query"
                  label-width="62px"
                  @submit.prevent
                >
                  <el-form-item label="名称" prop="name$VLK">
                    <el-input
                      v-model="query.name$VLK"
                      placeholder="请输入名称"
                    />
                  </el-form-item>
                  <el-form-item label="编码" prop="code$VLK">
                    <el-input
                      v-model="query.code$VLK"
                      placeholder="请输入编码"
                    />
                  </el-form-item>
                  <el-form-item
                    v-show="collapse"
                    label="状态"
                    prop="enabled$NEQ"
                  >
                    <el-select v-model="query.enabled$NEQ">
                      <el-option label="请选择" value="" />
                      <el-option label="启用" value="1" />
                      <el-option label="禁用" value="0" />
                    </el-select>
                  </el-form-item>
                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="search()">
                      查询
                    </el-button>
                    <el-button :icon="RefreshRight" @click="reset()">
                      重置
                    </el-button>
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
                <el-space>
                  <router-link
                    v-ab-btn-rights:roleManager_add
                    :to="{
                      name: 'RoleEdit',
                      query: { typeCode: query['typeCode$VEQ'] },
                    }"
                  >
                    <el-button :icon="Plus" type="primary">添加</el-button>
                  </router-link>
                  <el-button
                    v-ab-btn-rights:roleManager_batchDel
                    :disabled="!selectedData || selectedData.length == 0"
                    :icon="Delete"
                    type="danger"
                    @click="delBySeletedIds(orgApi.role.orgRoleRemoveUrl)"
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
            :url="orgApi.role.orgRoleListUrl"
          >
            <ab-column label="名称" min-width="120" prop="name" />
            <ab-column label="编码" prop="code" width="180" />
            <ab-column label="分组" prop="typeName" width="160" />
            <ab-column label="级别" prop="level" sortable width="80" />
            <ab-column
              ab-text-formatter="1-启用-success-dark|0-禁用-danger-dark"
              label="状态"
              prop="enabled"
              width="80"
            />
            <ab-column
              ab-date-formatter="yyyy-MM-dd HH:mm"
              label="创建时间"
              prop="createTime"
              sortable
              width="170"
            />
            <ab-column
              ab-template="edit"
              fixed="right"
              label="操作"
              width="410"
            />
            <template #edit="{ scope }">
              <router-link
                v-ab-btn-rights:roleManager_edit
                :to="{ name: 'RoleEdit', query: { id: scope.row.id } }"
              >
                <el-button text type="primary">编辑</el-button>
              </router-link>
              <el-button
                v-ab-btn-rights:roleManager_resource
                text
                type="primary"
                @click="openRoleResGrantDialog(scope.row)"
              >
                角色资源
              </el-button>
              <router-link
                v-ab-btn-rights:roleManager_userList
                :to="{
                  name: 'RoleUserList',
                  query: { roleId: scope.row.id, roleName: scope.row.name },
                }"
              >
                <el-button text type="primary">分配用户</el-button>
              </router-link>
              <el-button
                v-ab-btn-rights:roleManager_delete
                text
                type="primary"
                @click="
                  sendAction(
                    orgApi.role.orgRoleRemoveUrl + scope.row.id,
                    `确定删除${scope.row.name}吗?`
                  )
                "
              >
                删除
              </el-button>
            </template>
          </ab-table>
        </div>
      </el-main>
    </el-container>
    <!-- 角色资源授权 -->
    <role-res-grant
      :roleRes="roleResGrantInfo"
      :role-id="roleResGrantInfo.roleId"
    />
  </div>
</template>

<script lang="ts">
  import { abTableMix, abDictTree, orgApi } from 'agilebpm'
  export default { name: 'RoleList', mixins: [abTableMix] }
</script>

<script lang="ts" setup>
  import { reactive } from 'vue'
  import {
    Search,
    Delete,
    Plus,
    RefreshRight,
    ArrowUp,
    ArrowDown,
  } from '@element-plus/icons-vue'
  import RoleResGrant from '@/views/org/role/roleResGrant.vue'

  const roleResGrantInfo = reactive({
    roleId: '',
    visible: false,
  })

  // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
  const query: any = reactive({
    name$VLK: '',
    code$VLK: '',
    typeCode$VEQ: '',
    enabled$NEQ: '',
  })

  /**
   * 打开角色资源授权对话框
   * @param role 角色
   */
  const openRoleResGrantDialog = (role: any) => {
    roleResGrantInfo.visible = true
    roleResGrantInfo.roleId = role.id
  }
</script>
