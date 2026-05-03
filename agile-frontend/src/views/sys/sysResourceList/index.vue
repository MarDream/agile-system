<template>
  <div class="common-layout" style="overflow: hidden;">
    <div class="left-box" width="350px">
      <tree
        ref="treeRef"
        :loading-des="state.loading"
        @add-fn="addaFn"
        @enable-app-fn="enableAppFn"
        @reset-fields="resetFields"
        @update-api-button-data="updateApiButtonDataFn"
        @update-fetchdata="updatedesdata"
        @update-loading="loadingFn"
      />
    </div>
    <div class="right-box">
      <div v-show="state.form.parentId">
        <div v-if="state.isSave" class="flex" style="padding: 15px">
          <el-button
            :loading="state.saveloading"
            type="primary"
            @click="saveFn"
          >
            保存
          </el-button>
          <el-button @click="cancelFn">取消</el-button>
        </div>
        <div v-else class="flex" style="padding: 15px">
          <el-button type="primary" @click="editFn">编辑</el-button>
          <!-- <el-button
              v-if="state.form.id && state.form.id.length > 0"
              @click="removeFn"
            >
              删除
            </el-button> -->
        </div>
        <el-divider style="margin: 0 0 20px 0" />
        <el-form
          ref="formRef"
          v-loading="state.loading"
          :inline="false"
          label-width="100px"
          :model="state.form"
          :rules="state.rules"
        >
          <el-form-item label="资源名" prop="name">
            <ab-pinyin
              v-model="state.form.name"
              v-model:to="state.form.code"
              :disabled="!state.isSave"
            />
          </el-form-item>
          <el-form-item label="父资源名" prop="parentName">
            <span>{{ state.form.parentName }}</span>
          </el-form-item>
          <el-form-item
            v-if="state.BizValidatorMap.varirule"
            label="编码"
            prop="code"
            :rules="[
              { required: true, message: '必填' },
              state.BizValidatorMap.varirule.rule,
            ]"
          >
            <el-input
              v-model="state.form.code"
              :disabled="!state.isSave"
              placeholder="资源编码，全局唯一"
            />
          </el-form-item>
          <el-form-item label="请求地址" prop="url">
            <el-tooltip
              class="item"
              content="按钮、链接需要正确配置请求链接，该链接会用于系统鉴权"
              effect="dark"
              placement="right-end"
            >
              <el-input
                v-model="state.form.url"
                :disabled="!state.isSave"
                placeholder="请输入请求地址"
              />
            </el-tooltip>
          </el-form-item>
          <!-- <el-form-item label="类型" prop="type">
            <el-radio-group v-model="state.form.type" :disabled="!state.isSave">
              <el-radio-button label="menu">菜单</el-radio-button>
              <el-radio-button label="button">按钮</el-radio-button>
              <el-radio-button label="API">API</el-radio-button>
            </el-radio-group>
          </el-form-item> -->
          <el-form-item label="状态" prop="enable">
            <el-radio-group
              v-model="state.form.enable"
              :disabled="!state.isSave"
            >
              <el-radio-button :label="0">禁用</el-radio-button>
              <el-radio-button :label="1">启用</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="默认展开" prop="opened">
            <el-radio-group
              v-model="state.form.opened"
              :disabled="!state.isSave"
            >
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            v-if="state.enableApp == 1"
            label="手机端图标"
            prop="icon"
          >
            <ab-choose-svg v-model="state.form.icon" />
            <svg-icon
              :icon="state.form.icon"
              style="width: 30px; height: 30px; margin-left: 12px"
            />
          </el-form-item>
          <el-form-item v-else label="字体图标" prop="icon">
            <el-input
                v-model="state.form.icon"
                placeholder="请输入字体图标"
              />
            <!-- <el-icon>
              <component :is="'el-icon-menu'"/>
            </el-icon> -->
            <!-- <choose-icon-new
              v-model="state.form.icon"
              :disabled="!state.isSave"
            /> -->
          </el-form-item>

          <el-form-item label="排序">
            <span>保存后自动生成</span>
          </el-form-item>
          <el-form-item label="关联资源">
            <el-table
              border="1"
              :data="state.apiButtonTableData"
              style="width: 90%"
              
            >
              <el-table-column label="资源名" prop="name" width="">
                <template #default="scope">
                  <el-input
                    v-model="scope.row.name"
                    placeholder="请输入资源名"
                    size="small"
                  />
                </template>
              </el-table-column>
              <el-table-column label="编码" prop="code">
                <template #default="scope">
                  <el-input v-model="scope.row.code" placeholder="请输入编码"  size="small"/>
                </template>
              </el-table-column>
              <el-table-column label="鉴权地址" prop="url">
                <template #default="scope">
                  <el-input
                    v-model="scope.row.url"
                    placeholder="请输入鉴权地址"
                    size="small"
                  />
                </template>
              </el-table-column>
              <el-table-column label="类型" prop="type" width="150">
                <template #default="scope">
                  <el-radio-group v-model="scope.row.type"  size="small">
                    <el-radio-button label="button">按钮</el-radio-button>
                    <el-radio-button label="API">API</el-radio-button>
                  </el-radio-group>
                </template>
              </el-table-column>
              <el-table-column label="状态" prop="enable" width="150">
                <template #default="scope">
                  <el-radio-group v-model="scope.row.enable" size="small">
                    <el-radio-button :label="0">禁用</el-radio-button>
                    <el-radio-button :label="1">启用</el-radio-button>
                  </el-radio-group>
                </template>
              </el-table-column>
              <el-table-column label="操作" prop="enable" width="120">
                <template #header>
                  <span>操作</span>
                  <el-button
                    size="small"
                    style="float: right"
                    type="primary"
                    @click="addApi"
                  >
                    添加
                  </el-button>
                </template>
                <template #default="scope">
                  <el-button
                    size="small"
                    type="text"
                    @click="delApiButton(scope.row, scope.$index)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-form>
      </div>
      <div v-show="!state.form.parentId">
        <el-empty description="点击左侧资源查看详情" />
      </div>
    </div>
  </div>
</template>

<script setup >
  import { reactive, ref, getCurrentInstance } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import tree from './components/tree.vue'
  // import RemixIcon from './components/remixIcon.vue'
  import { abChooseSvg, chooseIconNew, svgIcon } from 'agilebpm'
  const $baseLoading = inject('$baseLoading')
  const formRef = ref('formRef')
  const treeRef = ref('treeRef')
  import { bizApi, authApi } from 'agilebpm'
  const state = reactive({
    BizValidatorMap: {},
    form: {
      name: '',
      code: '',
      url: '',
      type: 'menu',
      enable: 1,
      opened: 0,
      icon: '',
      children: [],
      checked: false,
    },
    rules: {
      name: [{ required: true, trigger: 'blur', message: '必填' }],
      code: [{ required: true, trigger: 'blur', message: '必填' }],
    },
    saveloading: false,
    loading: false,
    isSave: false,
    enableApp: 0,
    apiButtonTableData: [],
  })
  bizApi.bizPattern.getAllBizValidator().then((data) => {
    state.BizValidatorMap = data
  })
  const addApi = () => {
    state.apiButtonTableData.unshift({
      name: '',
      code: '',
      url: '',
      type: 'button',
      enable: 1,
      parentId: state.form.id,
      appId: state.form.appId,
    })
  }
  // 更新右侧详细信息
  const updatedesdata = (data) => {
    state.isSave = true
    state.form = data
  }
  // loading框控制
  const loadingFn = (bool) => {
    state.loading = bool
  }
  // 点击编辑
  const editFn = () => {
    state.isSave = true
  }
  // 取消
  const cancelFn = () => {
    formRef.value.clearValidate()
    state.isSave = false
  }
  // 保存
  const saveFn = () => {
    formRef.value.validate(async (valid) => {
      if (valid) {
        state.saveloading = true
        try {
          authApi.saveResource(state.form).then(
            (data) => {
              state.form.children =
                state.apiButtonTableData.length > 0
                  ? state.apiButtonTableData
                  : []
              if (state.form.children.length > 0) {
                state.form.children.forEach((item) => {
                  item.parentId = data.data
                })
              }
              authApi.saveTreeUrl(state.form).then(
                (dataa) => {
                  ElMessage({
                    type: 'success',
                    message: '保存成功',
                    onClose: () => {},
                  })
                  state.saveloading = false
                  // 刷新树形
                  treeRef.value.fetchTreeDataAndData(data.data)
                  state.isSave = false
                },
                () => {
                  state.saveloading = false
                }
              )
            },
            () => {
              state.saveloading = false
            }
          )
        } catch (err) {
          state.saveloading = false
        }
      }
    })
  }
  const resetFields = () => {
    state.form = {}
  }
  // 点击添加
  const addaFn = (row) => {
    state.isSave = true
    state.loading = true
    setTimeout(() => {
      formRef.value.resetFields()
      state.form = {
        name: '',
        code: '',
        url: '',
        type: 'menu',
        enable: 1,
        opened: 0,
        icon: '',
        children: [],
        checked: false,
      }
      state.apiButtonTableData = []
      Object.assign(state.form, {
        appId: row.appId,
        parentId: row.id,
      })
      state.form.parentName = row.name
      state.loading = false
    }, 500)
  }
  const enableAppFn = (enableApp) => {
    state.enableApp = enableApp
    state.form.parentId = false
  }
  const updateApiButtonDataFn = (data) => {
    state.apiButtonTableData = data
  }
  const delApiButton = (row, index) => {
    ElMessageBox.confirm(`确定删除【${row.name}】这个${row.type}吗`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(() => {
      if (row.id) {
        authApi.removeResource({ id: row.id }).then(({ msg }) => {
          ElMessage({
            type: 'success',
            message: '已删除',
            onClose: () => {},
          })
          // 刷新树形
          treeRef.value.fetchTreeDataAndData(row.parentId)
        })
      } else {
        state.apiButtonTableData.splice(index, 1)
      }
    })
  }
</script>

<style lang="scss" scoped>
  .selectstyle {
    width: 100%;
    margin-bottom: 10px;
  }
  .custom-tree-node {
    display: flex;
    flex: 1;
    align-items: center;
    justify-content: space-between;
    padding-right: 8px;
    font-size: 14px;
  }
  .el-input {
    max-width: 500px;
  }
  .left-box {
    float: left;
    width: 350px;
    border-right: 10px solid #f5f7f9;
  }
  .right-box {
    float: left;
    width: calc(100% - 350px);
  }
</style>
