<template>
  <el-container class="layout-container-demo">
    <el-header>
      <ab-save
        back-name="CustomDialogList"
        :back-other-fn="backOtherFn"
        :custom="
          props.dialogSetting && props.dialogSetting.custom
            ? props.dialogSetting.custom
            : false
        "
        :form-ref="formRef"
        :save-data="info.data"
        :url="bizApi.customDialog.bizCustDialogSave"
        :valid-after-fn="validaftersave"
      />
      <ab-load
        v-model="info.data"
        :url="bizApi.customDialog.bizCustDialogGet"
        @after-fn="getDataFn"
      />
      <el-button
        v-if="info.data.objName.length > 0 && info.data.style.length > 0"
        style="margin-left: 12px"
        type="primary"
        @click="openpar"
      >
        设置字段参数
      </el-button>
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-scrollbar>
        <el-form
          ref="formRef"
          label-suffix="："
          label-width="150px"
          :model="info.data"
        >
          <el-row>
            <el-col :span="9" style="margin-right: 35px">
              <el-form-item
                label="名称"
                m-item
                prop="name"
                :rules="[{ required: true, message: '必填' }]"
              >
                <ab-pinyin
                  v-model="info.data.name"
                  v-model:to="info.data.code"
                />
              </el-form-item>
              <el-form-item label="描述" prop="desc">
                <el-input v-model="info.data.desc" :rows="4" type="textarea" />
              </el-form-item>
              <el-form-item
                label="显示类型"
                prop="style"
                :rules="[{ required: true, message: '必选' }]"
              >
                <el-select
                  v-model="info.data.style"
                  placeholder="请选择"
                  style="width: 100%"
                  @change="changeStyle"
                >
                  <el-option
                    v-for="item in info.BizCustDialogStyleOption"
                    :key="item.key"
                    :label="item.desc"
                    :value="item.key"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="单选/多选" prop="multiple">
                <el-switch
                  v-model="info.data.multiple"
                  active-text="多选"
                  :active-value="1"
                  inactive-text="单选"
                  :inactive-value="0"
                />
              </el-form-item>
              <el-form-item label="左侧树" prop="leftTree">
                <el-switch
                  v-model="info.data.leftTree"
                  active-text="开启"
                  :active-value="1"
                  inactive-text="关闭"
                  :inactive-value="0"
                  @change="changeLeftTree"
                />
              </el-form-item>

              <el-form-item
                v-if="info.data.leftTree"
                label="左侧树配置"
                prop="leftTreeConfigJson"
              >
                <!-- 数据字典 -->
                <ab-select-tree
                  v-if="
                    info.leftTreeData &&
                    info.leftTreeData.length > 0 &&
                    info.data.leftTreeConfig.type === 'dataDict'
                  "
                  v-model="info.data.leftTreeConfig.key"
                  :is-cust="true"
                  style="width: 100%"
                  :tree-data="info.leftTreeData"
                />
                <!-- 树形对话框 -->
                <el-tag
                  v-if="
                    info.data.leftTreeConfig.type === 'bizCustDialog' &&
                    info.data.leftTreeConfig.key.length > 0
                  "
                >
                  {{ info.data.leftTreeConfig.key }} -
                  {{ info.data.leftTreeConfig.name }}
                </el-tag>
                <ab-cust-dialog
                  v-if="info.data.leftTreeConfig.type === 'bizCustDialog'"
                  dialog-key="custDialog"
                  :dialog-setting="{ multiple: false }"
                  :param="{ style_$VEQ: 'tree' }"
                  style="display: inline-block"
                  @ok="choosezdydhklb"
                >
                  选择
                </ab-cust-dialog>
              </el-form-item>

              <el-form-item v-if="info.data.leftTree" label="左侧树根节点">
                <el-input
                  v-model="info.data.leftTreeConfig.rootName"
                  placeholder="请输入左侧树根节点"
                />
              </el-form-item>
              <el-form-item
                label="数据来源"
                prop="dataSource"
                :rules="[{ required: true, message: '必选' }]"
              >
                <el-select
                  v-model="info.data.dataSource"
                  :disabled="info.data.id ? true : false"
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in info.BizCustDialogSourceTypeOption"
                    :key="item.key"
                    :label="item.desc"
                    :value="item.key"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'database'"
                label="数据源"
                prop="source"
              >
                <el-select
                  v-model="info.data.dsKey"
                  :disabled="info.data.id ? true : false"
                  placeholder="请选择"
                  style="width: 100%"
                  @change="changesource"
                >
                  <el-option
                    v-for="item in info.dataSourceOptions"
                    :key="item.alias"
                    :label="item.name"
                    :value="item.alias"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'database'"
                label="查询类型"
                prop="objType"
                :rules="[{ required: true, message: '必选' }]"
              >
                <el-select
                  v-model="info.data.objType"
                  :disabled="info.data.id ? true : false"
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in info.BizCustDialogObjTypeOption"
                    :key="item.key"
                    :label="item.desc"
                    :value="item.key"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'database'"
                label="表名"
                prop="objName"
                :rules="[{ required: true, message: '必填' }]"
              >
                <el-input
                  v-model="info.data.objName"
                  class="input-with-select"
                  clearable
                  :disabled="info.data.id ? true : false"
                  placeholder="请输入关键字查询"
                >
                  <template #append>
                    <el-button :icon="Search" @click="searchList" />
                  </template>
                </el-input>
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'database' && !info.data.id"
                label="选择对象"
                prop="value"
              >
                <el-select
                  v-model="info.data.objName"
                  clearable
                  :disabled="info.data.id ? true : false"
                  :fit-input-width="true"
                  placeholder="请选择"
                  style="width: 100%"
                  @change="changesourceType"
                >
                  <el-option
                    v-for="item in info.objNameOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'elasticsearch'"
                label="集合名"
                prop="objName"
                :rules="[{ required: true, message: '必选' }]"
              >
                <el-input
                  v-model="info.data.objName"
                  placeholder="请输入集合名"
                />
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'http'"
                label="url地址"
                prop="objName"
                :rules="[{ required: true, message: '必选' }]"
              >
                <el-input
                  v-model="info.data.objName"
                  placeholder="请输入url地址"
                />
              </el-form-item>
              <el-form-item
                v-if="info.data.dataSource === 'interface'"
                label="接口"
                prop="objName"
                :rules="[{ required: true, message: '必选' }]"
              >
                <el-input
                  v-model="info.data.objName"
                  placeholder="beanId.method eg:userService.list"
                />
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item
                label="编码"
                prop="code"
                :rules="[
                  { required: true, message: '必填' },
                  { max: 50, message: '最多可输入50个字符' },
                  info.rule,
                ]"
              >
                <ab-code
                  v-model="info.data.code"
                  :disabled="info.data.id ? true : false"
                  placeholder="请输入编码"
                />
              </el-form-item>
              <el-form-item
                label="分类"
                prop="typeCode"
                :rules="[{ required: true, message: '必填' }]"
              >
                <ab-select-tree v-model="info.data.typeCode" type-code="biz" />
              </el-form-item>
              <el-form-item label="是否内置" prop="system">
                <template #label>
                  <div>
                    是否内置
                    <el-popover
                      content="系统内部对话框，则表示项目本身需要这个对话框，用户请谨慎修改，否则有可能会导致系统功能异常"
                      placement="bottom-start"
                      trigger="hover"
                    >
                      <template #reference>
                        <el-icon><info-filled style="color: red" /></el-icon>
                      </template>
                    </el-popover>
                  </div>
                </template>
                <el-switch
                  v-model="info.data.system"
                  active-text="是"
                  :active-value="1"
                  inactive-text="否"
                  :inactive-value="0"
                  inline-prompt
                />
              </el-form-item>
              <el-form-item
                label="尺寸"
                prop="width"
                :rules="[{ required: true, message: '宽高必填' }]"
              >
                <el-input
                  v-model="info.data.width"
                  placeholder="宽度"
                  style="display: inline-block; max-width: 80px"
                />
                <el-input
                  v-model="info.data.height"
                  placeholder="高度"
                  style="display: inline-block; width: 80px"
                />
              </el-form-item>
              <el-form-item
                v-if="info.data.style === 'list'"
                label="分页"
                prop="pageSize"
                :rules="[{ required: true, message: '必填' }]"
              >
                <el-switch
                  v-model="info.data.page"
                  active-text="是"
                  :active-value="1"
                  inactive-text="否"
                  :inactive-value="0"
                  inline-prompt
                  style="margin-right: 12px"
                />
                <el-input
                  v-if="info.data.page"
                  v-model="info.data.pageSize"
                  placeholder="分页大小"
                  style="display: inline-block; max-width: 150px"
                />
              </el-form-item>
              <el-form-item v-if="info.data.leftTree" label="类型">
                <el-select
                  v-model="info.data.leftTreeConfig.type"
                  placeholder="请选择"
                  style="width: 100%"
                  @change="changeLeftTreeConfigType"
                >
                  <el-option
                    v-for="item in info.BizCustDialogLeftTreeConfigTypeOption"
                    :key="item.key"
                    :label="item.desc"
                    :value="item.key"
                  />
                </el-select>
              </el-form-item>
              <el-form-item v-if="info.data.leftTree" label="关联条件字段">
                <el-button
                  v-if="info.data.conditionFields.length <= 0"
                  size="small"
                  type="primary"
                >
                  请先配置对话框的条件字段
                </el-button>
                <el-select
                  v-if="
                    info.data.conditionFields &&
                    info.data.conditionFields.length > 0
                  "
                  v-model="info.data.leftTreeConfig.condField"
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in info.data.conditionFields"
                    :key="item.columnName"
                    :label="item.showName"
                    :value="item.columnName"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-scrollbar>
    </el-main>
    <!-- class="alignCenterDialog" -->
    <el-dialog
      v-model="info.dialogVisible"
      :align-center="true"
      :destroy-on-close="true"
      title="设置字段参数"
      width="1300px"
    >
      <Parameters
        v-if="info.data.style"
        ref="parametersref"
        :data="info.data"
        :parameters-obj="info.parametersObj"
        @update-table-data="updateParameters"
      />
      <template #footer>
        <span class="dialog-footer">
          <el-button
            style="margin-right: 0"
            text
            type="primary"
            @click="info.dialogVisible = false"
          >
            取消
          </el-button>
          <el-button type="primary" @click="saveDialog">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup lang="ts">
  import { reactive, ref, onMounted } from 'vue'
  import { ElMessage } from 'element-plus'
  import { sysApi, bizApi, getData, postData } from 'agilebpm'
  import { Search } from '@element-plus/icons-vue'
  import type { FormInstance } from 'element-plus'
  import Parameters from './components/parameters.vue'
  // emit 事件，父类根据事件对自身进行更新
  const emit = defineEmits(['closeFn'])
  const parametersref = ref('parametersref')
  const selectRef = ref('selectRef')
  const formRef = ref<FormInstance>()
  const systreeref = ref()
  const props = defineProps({
    dialogSetting: {
      type: Object,
      default: null,
    },
  })
  const info: any = reactive({
    BizCustDialogStyleOption: [],
    BizCustDialogLeftTreeConfigTypeOption: [],
    leftTreeConfigTreeOption: [],
    BizCustDialogSourceTypeOption: [],
    dataSourceOptions: [],
    BizCustDialogObjTypeOption: [],
    rule: null,
    treekey: '',
    treename: '',
    data: {
      name: '',
      code: '',
      typeCode: '',
      desc: '',
      system: 0,
      style: '',
      width: '800',
      height: '750',
      multiple: 0,
      page: 1,
      pageSize: '10',
      leftTree: 0,
      leftTreeConfig: {
        condField: '',
        key: '',
        name: '',
        rootName: '所有数据',
        type: '',
      },
      dataSource: '',
      objType: '',
      objName: '',
      dsKey: '',
      fieldsList: [],
      treeConfig: {},
      displayFields: [],
      conditionFields: [],
      returnFields: [],
      sortFields: [],
    },
    dialogVisible: false,
    parametersObj: {},
    parameterstitle: '',
    displayFields: [],
    conditionFields: [],
    conditionFieldsOption: [],
    returnFields: [],
    sortFields: [],
    editDia: true,
    oneListData: {},
    updateRow: {},
    leftTreeData: [],
  })
  // 显示类型
  sysApi.tools
    .getEnum('com.dstz.biz.api.constant.BizCustDialogStyle', true)
    .then(({ data }) => {
      info.BizCustDialogStyleOption = data
      // info.data.style = info.BizCustDialogStyleOption[0].key
    })

  // 类型
  sysApi.tools
    .getEnum('com.dstz.biz.api.constant.BizCustDialogLeftTreeConfigType', true)
    .then(({ data }) => {
      info.BizCustDialogLeftTreeConfigTypeOption = data
    })

  const changeLeftTree = (row: any) => {
    if (row === 1) {
      info.data.leftTreeConfig.type =
        info.BizCustDialogLeftTreeConfigTypeOption[0].key
    } else {
      info.data.leftTreeConfig.type = ''
    }
  }
  // 数据来源
  sysApi.tools
    .getEnum('com.dstz.biz.api.constant.BizCustDialogSourceType', true)
    .then(({ data }) => {
      info.BizCustDialogSourceTypeOption = data
      // info.data.dataSource = info.BizCustDialogSourceTypeOption[0].key
    })

  // 查询
  sysApi.tools
    .getEnum('com.dstz.biz.api.constant.BizCustDialogObjType', true)
    .then(({ data }) => {
      info.BizCustDialogObjTypeOption = data
      // info.data.objType = info.BizCustDialogObjTypeOption[0].key
    })

  //  条件
  sysApi.tools
    .getEnum('com.dstz.base.query.ConditionType', true)
    .then(({ data }) => {
      info.parametersObj.ConditionTypeOption = data
    })

  //  值来源
  sysApi.tools
    .getEnum(
      'com.dstz.biz.api.constant.BizCustDialogConditionFieldValueSource',
      true
    )
    .then(({ data }) => {
      info.parametersObj.BizCustDialogConditionOption = data
    })

  // 保存前校验
  const validaftersave = () => {
    if (info.data.style == 'tree') {
      let columns = []
      if (
        info.parametersObj.tableData &&
        info.parametersObj.tableData.columns
      ) {
        columns = info.parametersObj.tableData.columns
      }
      const id = info.data.treeConfig.id
      const pid = info.data.treeConfig.pid
      info.data.treeConfig.idType = null
      info.data.treeConfig.pidType = null
      if (columns && columns.length > 0) {
        columns.forEach((item: any) => {
          if (id.toUpperCase() == item.name.toUpperCase()) {
            info.data.treeConfig.idType = item.type
          }
          if (pid.toUpperCase() == item.name.toUpperCase()) {
            info.data.treeConfig.pidType = item.type
          }
        })
      }
    }
    if (info.data.leftTree === 1) {
      if (info.data.leftTreeConfig.condField.length <= 0) {
        ElNotification({
          title: '错误提示',
          message: '开启左侧树时需要配置关联条件字段',
          type: 'error',
        })
        return false
      }
    }
    if (info.data.style === 'list') {
      if (info.data.displayFields.length <= 0) {
        ElNotification({
          title: '错误提示',
          message: '保存时请完善显示字段信息',
          type: 'error',
        })
        return false
      }
    }
    if (info.data.style === 'tree') {
      if (
        (!info.data.treeConfig.id && info.data.treeConfig.id.length <= 0) ||
        (!info.data.treeConfig.pid && info.data.treeConfig.pid.length <= 0) ||
        (!info.data.treeConfig.showColumn &&
          info.data.treeConfig.showColumn.length <= 0)
      ) {
        ElNotification({
          title: '错误提示',
          message: '保存时请完善显示字段信息',
          type: 'error',
        })
        return false
      }
    }
    if (info.data.returnFields.length <= 0) {
      ElNotification({
        title: '错误提示',
        message: '保存时请完善返回字段信息',
        type: 'error',
      })
      return false
    }

    return true
  }

  // 校验表单
  bizApi.bizPattern.getAllBizValidator().then((data: any) => {
    info.rule = data.varirule.rule
  })

  const changeStyle = (value: any) => {
    if (value === 'tree') {
      info.data.page = 0
    }
  }

  //切换为数据字典类型
  const changeLeftTreeConfigType = (item: any) => {
    info.data.leftTreeConfig.key = ''
    systreeref.value.reset()
  }

  // 类型为数据字典时请求
  const queryDictTypeTree = () => {
    postData(sysApi.dict.getDictTypeTreeUrl, {}).then(
      (result) => {
        info.leftTreeData = result.data
      },
      () => {}
    )
  }

  // 选择分类
  const selectNodeClickFn = (row: any) => {
    // eslint-disable-next-line no-empty
    if (row.typeCode === 'system') {
    } else {
      info.data.leftTreeConfig.name = row.name
      info.data.leftTreeConfig.key = row.code
      selectRef.value.visible = false
    }
  }

  // 点击设置参数按钮
  const openpar = () => {
    info.dialogVisible = true
    queryOpenFn()
  }

  // 设置参数按钮请求接口
  const queryOpenFn = () => {
    postData(bizApi.customDialog.bizCustDialogGetTable, {
      dsKey: info.data.dsKey,
      objType: info.data.objType,
      objName: info.data.objName,
      dataSource: info.data.dataSource,
    }).then(
      (result) => {
        info.parametersObj.tableData = result.data.columns[0].name
          ? result.data
          : { columns: [] }
      },
      () => {}
    )
  }

  //获取数据源列表
  const getdataSourceList = () => {
        info.dataSourceOptions = [{alias:"dataSourceDefault",name:"本地数据源"}];
        if (!info.data.dsKey || info.data.dsKey.length <= 0) {
          info.dataSourceOptions.forEach(
            (element: { name: string; alias: any }) => {
              if (element.name === '本地数据源') {
                info.data.dsKey = element.alias
                info.data.dsName = element.name
              }
            }
          )
        }
  }

  const changesource = (value: any) => {
    info.dataSourceOptions.forEach((element: { name: string; alias: any }) => {
      if (value === element.alias) {
        info.data.dsName = element.name
      }
    })
  }

  // 点击查询
  const searchList = () => {
    if (!info.data.objType || info.data.objType.length <= 0) {
      ElMessage({
        showClose: true,
        message: '请先选择查询类型',
        type: 'error',
      })
      return false
    }
    formRef.value.validateField('objName', (valid: boolean) => {
      if (valid) {
        getData(bizApi.bizTable.BizTableSearchObjName, {
          dsKey: info.data.dsKey,
          objType: info.data.objType,
          objName: info.data.objName,
        }).then(
          ({ data }) => {
            if (data && JSON.stringify(data) !== '{}') {
              const arr = []
              for (const key in data) {
                arr.push({
                  value: key,
                  label: `${key}(${data[key]})`,
                })
              }
              info.objNameOptions = arr
              info.data.objName = info.objNameOptions[0].value
            } else {
              ElMessage({
                showClose: true,
                message: '查询结果为空',
                type: 'error',
              })
            }
          },
          () => {}
        )
      }
    })
  }

  // 切换选择对象
  const changesourceType = (val: any) => {}

  // 请求对话框接口
  const getByCodeFn = () => {
    getData(`${bizApi.customDialog.getByCode}custDialog`, {}).then(
      (result) => {
        info.oneListData = result.data
      },
      () => {}
    )
  }

  const updateRowFn = (row: any) => {
    info.updateRow = row
  }

  const savetreeDialog = () => {
    info.treedialogVisible = false
    info.data.leftTreeConfig.key = info.updateRow.code_
    info.data.leftTreeConfig.name = info.updateRow.name_
  }

  // 打开树形对话框
  const openbizCustDialog = () => {
    info.treedialogVisible = true
    getByCodeFn()
  }

  // 获取datatree数据
  const getTreeData = async () => {
    const result = await sysApi.dict.getDictTreeUrl({
      queryParam: { typeCode$VEQ: 'system' },
    })
    info.leftTreeConfigTreeOption = result.data
  }

  const saveDialog = (row: any) => {
    info.dialogVisible = false
    parametersref.value.emitFn()
    info.data.treeConfig = info.treeConfig
    info.data.displayFields = info.displayFields
    info.data.conditionFields = info.conditionFields
    info.conditionFieldsOption = info.conditionFields.filter((item: any) => {
      return !item.value.ctrlType && item.valueSource == 'param'
    })
    info.data.returnFields = info.returnFields
    info.data.sortFields = info.sortFields

    info.parametersObj.treeConfig = info.treeConfig
    info.parametersObj.displayFields = info.displayFields
    info.parametersObj.conditionFields = info.conditionFields
    info.parametersObj.returnFields = info.returnFields
    info.parametersObj.sortFields = info.sortFields
  }

  const updateParameters = (
    treeConfig: any,
    displayFields: any,
    conditionFields: any,
    returnFields: any,
    sortFields: any
  ) => {
    info.treeConfig = treeConfig
    info.displayFields = displayFields
    info.conditionFields = conditionFields
    info.returnFields = returnFields
    info.sortFields = sortFields
  }
  const getDataFn = (data: any) => {
    info.parametersObj.treeConfig = data.treeConfig
    info.parametersObj.displayFields = data.displayFields
    info.parametersObj.conditionFields = data.conditionFields
    info.parametersObj.returnFields = data.returnFields
    info.parametersObj.sortFields = data.sortFields
    queryOpenFn()
  }
  // do not use same name with ref
  const choosezdydhklb = (list: any) => {
    info.data.leftTreeConfig.key = list[0].code
    info.data.leftTreeConfig.name = list[0].name
  }

  const backOtherFn = () => {
    emit('closeFn')
  }
  onMounted(() => {
    getTreeData()
    getdataSourceList()
    queryDictTypeTree()
  })
</script>
<style lang="scss">
  .heightformitem {
    height: 94px;
  }
  .heightformitem .el-form-item__label {
    line-height: 94px !important;
  }
</style>
