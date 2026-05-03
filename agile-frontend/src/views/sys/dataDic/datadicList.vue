<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="left-tree">
        <ab-dict-tree
          v-model="info.query.queryParam.typeCode$VIN"
          type-code="biz"
          @node-click="nodeClickFn"
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
                  :model="info.query.queryParam"
                  @submit.prevent
                >
                  <el-form-item label="字典名称" prop="name$VLK">
                    <el-input
                      v-model="info.query.queryParam.name$VLK"
                      placeholder="请输入字典名称"
                      @keyup.enter.native="listFn()"
                    />
                  </el-form-item>
                  <el-form-item label="字典编码" prop="code$VLK">
                    <el-input
                      v-model="info.query.queryParam.code$VLK"
                      placeholder="请输入字典编码"
                      @keyup.enter.native="listFn()"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button :icon="Search" type="primary" @click="listFn()">
                      查询
                    </el-button>
                    <el-button :icon="RefreshRight" @click="resetquery()">
                      重置
                    </el-button>
                  </el-form-item>
                </el-form>
              </el-col>
              <el-col class="left-panel">
                <el-button
                  :icon="Plus"
                  type="primary"
                  @click="addClickFn"
                >
                  添加
                </el-button>
              </el-col>
            </el-row>
          </div>

          <el-table
            ref="abTable"
            v-loading="info.loading"
            border
            :data="info.tableData"
            :height="info.tableHeight"
            style="width: 100%; margin-top: 20px"
            @cell-click="clickTable"
          >
            <el-table-column label="字典名称" min-width="120" prop="name" />
            <el-table-column label="字典Key" min-width="120" prop="code" />
            <el-table-column
              align="center"
              label="是否内置"
              prop="isSystem"
              width="90"
            >
              <template #default="scope">
                <el-tag
                  v-if="scope.row.isSystem == 1"
                  effect="dark"
                  type="success"
                >
                  是
                </el-tag>
                <el-tag
                  v-if="scope.row.isSystem == 0"
                  effect="dark"
                  type="warning"
                >
                  否
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column
              format="yyyy-MM-dd HH:mm"
              label="更新日期"
              min-width="120"
              prop="updateTime"
            />
            <el-table-column label="更新人" min-width="120" prop="updater" />
            <el-table-column
              align="left"
              fixed="right"
              label="操作"
              prop="key"
              width="140"
            >
              <template #default="scope">
                <el-button
                  text
                  type="primary"
                  @click.stop="editClickFn(scope.row)"
                >
                  编 辑
                </el-button>
                <el-button
                  v-if="scope.row.isSystem !== 1"
                  text
                  type="primary"
                  @click.stop="removeDict(scope.row)"
                >
                  删 除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <!--分页-->
          <el-pagination
            v-model:currentPage="info.query.currentPage"
            v-model:page-size="info.query.pageSize"
            :background="true"
            :disabled="false"
            layout=" prev, pager, next, jumper,total, sizes"
            :page-sizes="[2, 5, 10, 20, 30, 40, 50]"
            :total="info.total"
            @current-change="listFn"
            @size-change="listFn"
          />
        </div>
      </el-main>
      <el-aside
        v-loading="info.loadingdictData"
        class="right-tree"
        @contextmenu.prevent
      >
        <ab-tree
          v-if="info.dictTreeData.length > 0"
          :batch-operation="true"
          :dynamic-operation="info.dynamicOperation"
          :tree-data="info.dictTreeData"
          @batch-operation-click="batchOperationClickFn"
          @reload-click="dictreloadClickFn"
        >
          <p
            v-if="!info.dictTreeData[0].children"
            style="color: #ff4d4f; text-align: center"
          >
            （右键添加字典项）
          </p>
        </ab-tree>
        <div v-else>
          <el-empty description="请选择要维护的数据字典" :image-size="120" />
        </div>
      </el-aside>
    </el-container>
    <el-dialog v-model="info.dialogVisible" title="数据字典" width="30%">
      <el-form
        ref="ruleFormRef"
        class="demo-ruleForm"
        label-width="70px"
        :model="info.ruleForm"
      >
        <el-form-item
          label="分类"
          prop="typeCode"
          :rules="[{ required: true, message: '必填' }]"
        >
          <ab-select-tree
            v-model="info.ruleForm.typeCode"
            :has-root="false"
            type-code="biz"
          />
        </el-form-item>
        <el-form-item
          label="名称"
          prop="name"
          :rules="[{ required: true, message: '必填' }]"
        >
          <ab-pinyin
            v-model="info.ruleForm.name"
            v-model:to="info.ruleForm.code"
            placeholder="请输入名称"
          />
        </el-form-item>
        <el-form-item
          label="编码"
          prop="code"
          :rules="[{ required: true, message: '必填' }]"
        >
          <ab-code
            v-model="info.ruleForm.code"
            :disabled="info.ruleForm.id"
            placeholder="请输入编码"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button text type="primary" @click="info.dialogVisible = false">
            取消
          </el-button>
          <el-button
            :loading="info.saveLoading"
            type="primary"
            @click="submitForm(ruleFormRef)"
          >
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="info.dictdialogVisible" title="字典项" width="500">
      <el-table
        class="dataDictList"
        :data="info.dictTableData"
        height="300"
        row-key="snRowKey"
        style="width: 100%"
      >
        <el-table-column label="序号" prop="sn" width="80">
          <template #default="scope">
            <el-input v-model="scope.row.sn" :min="1" type="number" />
          </template>
        </el-table-column>
        <el-table-column label="名称" prop="name">
          <template #default="scope">
            <ab-pinyin
              v-model="scope.row.name"
              v-model:to="scope.row.code"
              placeholder="请输入"
            />
          </template>
        </el-table-column>
        <el-table-column label="编码" prop="code">
          <template #default="scope">
            <el-input v-model="scope.row.code" placeholder="请输入" />
          </template>
        </el-table-column>
        <el-table-column label="是否禁用" prop="sn" width="90">
          <template #default="scope">
            <el-switch
              v-model="scope.row.disable"
              :active-value="1"
              :inactive-value="0"
            />
          </template>
        </el-table-column>
        <el-table-column label="添加" prop="code" width="90">
          <template #header>
            <span>添加</span>
            <el-button
              circle
              :icon="Plus"
              size="small"
              style="margin-left: 12px"
              type="primary"
              @click="addDictList"
            />
          </template>
          <template #default="scope">
            <el-button
              circle
              class="move-btn"
              :icon="Sort"
              size="small"
              type="primary"
            />
            <el-button
              circle
              :icon="Delete"
              size="small"
              type="danger"
              @click="removeDictList(scope.row, scope.$index)"
            />
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button
            text
            type="primary"
            @click="info.dictdialogVisible = false"
          >
            取消
          </el-button>
          <el-button
            :loading="info.dictsaveLoading"
            type="primary"
            @click="dictsubmitForm()"
          >
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
  import {
    // abTableMix,
    sysApi,
    abTools,
    getData,
    postData,
    abDictTree,
  } from 'agilebpm'
  import { remove } from 'lodash'
  // export default {
  //   mixins: [abTableMix],
  // }
</script>
<script setup lang="ts">
  // eslint-disable-next-line no-unused-vars
  import { reactive, defineComponent, ref, nextTick } from 'vue'
  import {
    Search,
    Plus,
    RefreshRight,
    Delete,
    Sort,
  } from '@element-plus/icons-vue'
  import Sortable from 'sortablejs'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import type { FormInstance } from 'element-plus'
  //引入 当前实例
  const { proxy } = abTools.useCurrentInstance()
  // 查询条件定义，如果ts 需要定义所有参数，这里覆盖父类，不是的话可以不用设置，父类已经定义了query 对象
  const ruleFormRef = ref<FormInstance>()

  const info: any = reactive({
    tableHeight: `${window.innerHeight - 340}px`,
    loading: false,
    total: 0,
    query: {
      pageSize: 10,
      currentPage: 1,
      sortColumn: '',
      sortOrder: '',
      queryParam: {
        name$VLK: '',
        typeCode$VIN: '',
      },
    },
    tableData: [],
    treeData: [],
    dialogVisible: false,
    ruleForm: {
      dictType: 'dict',
      typeName: '',
      typeCode: '',
      name: '',
      code: '',
    },
    options: [
      {
        label: '',
        options: [],
      },
    ],
    //根据是否是内置数据动态展示按钮配置
    dynamicOperation: {
      conlem: 'isSystem',
      conlemOption: [
        {
          conlemValue: 1,
          option: ['add', 'refresh'],
        },
        {
          conlemValue: 0,
          option: ['add', 'edit', 'delete', 'refresh'],
        },
      ],
    },

    saveLoading: false,
    treeDataLoading: false,
    loadingDictData: false,
    dictTreeData: [],
    dictKey: '',
    dictdialogVisible: false,
    dictsaveLoading: false,

    dictrow: {},
    isEdit: false,
    typeName: '',
    dictTableData: [],
    testcode: false,
  })
  // 对 双向绑定的值进行watch 监控变化
  watch(
    () => info.dialogVisible,
    (amount, prevamount) => {
      if (!amount) {
        info.ruleForm.name = ''
        info.ruleForm.code = ''
        info.ruleForm.typeName = info.typeName
        info.ruleForm.typeCode = info.query.queryParam.typeCode$VIN
      }
      nextTick(() => {
        ruleFormRef.value.clearValidate()
      })
    }
  )

  // const restaurants = ref<RestaurantItem[]>([])
  // const querySearch = (queryString: string, cb: any) => {
  //   const results = queryString
  //     ? restaurants.value.filter(createFilter(queryString))
  //     : restaurants.value
  //   // call callback function to return suggestions
  //   cb(results)
  // }
  // 请求左侧树列表
  const getDataTree = () => {
    sysApi.dict
      .getDictTreeUrl({
        queryParam: {
          typeCode$VEQ: 'system',
        },
      })
      .then((result) => {
        info.treeData = result.data
      })
  }
  // 点击添加
  const addaFn = (row: any) => {}
  // 请求列表
  const listFn = () => {
    info.loading = true
    postData(sysApi.dict.getDictListUrl, info.query).then((result) => {
      info.tableData = result.data.rows
      info.total = result.data.total
      info.loading = false
    })
  }
  const resetquery = () => {
    info.query.currentPage = 1
    info.query.queryParam.name$VLK = ''
    info.query.queryParam.code$VLK = ''
    listFn()
  }
  // 点击左侧树节点
  const nodeClickFn = (row: any) => {
    info.query.currentPage = 1
    if (row.parentId == '0') {
      info.query.queryParam.typeCode$VIN = ''
      info.ruleForm.typeCode = ''
      info.ruleForm.typeName = ''
      info.typeName = ''
      listFn()
      return
    }
    if (row.code) {
      info.ruleForm.typeCode = row.code
      info.ruleForm.typeName = row.name
      info.typeName = row.name
    } else {
      info.ruleForm.typeCode = ''
      info.ruleForm.typeName = ''
      info.typeName = ''
    }
    listFn()
  }

  // 点击添加
  const addClickFn = () => {
    info.ruleForm = { dictType: 'dict' }
    info.ruleForm.typeCode = info.query.queryParam.typeCode$VIN
    info.dialogVisible = true
  }
  // 点击编辑
  const editClickFn = (row: any) => {
    info.dialogVisible = true
    info.ruleForm = JSON.parse(JSON.stringify(row))
  }

  // 点击删除
  const removeDict = (row: any) => {
    if (row.id) {
      ElMessageBox.confirm('确定删除？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          getData(sysApi.dict.removeDataDictUrl + row.id, {}).then(
            ({ msg }) => {
              ElMessage({
                showClose: true,
                message: msg,
                type: 'success',
              })
              info.dictTreeData = []
              listFn()
            }
          )
        })
        .catch(() => {})
    }
  }

  // 提交表单
  const submitForm = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
      if (valid) {
        info.saveLoading = true
        postData(sysApi.dict.saveDataDictUrl, {
          ...info.ruleForm,
          dictKey: info.ruleForm.code,
        })
          .then(
            ({ msg }) => {
              ElMessage({
                showClose: true,
                message: '保存成功',
                type: 'success',
              })
              listFn()
              info.dialogVisible = false
              info.saveLoading = false
            },
            () => {
              info.saveLoading = false
            }
          )
          .catch(() => {
            info.saveLoading = false
          })
      } else {
        console.log('error submit!', fields)
      }
    })
  }
  const getDictData = () => {
    info.loadingDictData = true
    sysApi.dict.getDictTreeByCodeNoCache(info.dictKey).then((result) => {
      info.dictTreeData = result.data
      info.loadingDictData = false
    })
  }
  // 点击table
  const clickTable = (row: any, column: any, cell: any, event: any) => {
    nextTick(() => {
      info.dictKey = row.dictKey
      getDictData()
    })
  }
  // 点击批量操作
  const batchOperationClickFn = (row: any) => {
    info.dictdialogVisible = true
    rowDrop()
    info.dictrow = row
    info.dictTableData = []
    if (info.dictrow.children) {
      info.dictTableData = JSON.parse(JSON.stringify(info.dictrow.children))
      info.dictTableData.forEach((item: any, index: any) => {
        item.snRowKey = index + 1
        item.children = []
      })
    }
  }
  const isRepeat = (arr: any) => {
    const hash = {}
    for (const i in arr) {
      if (hash[arr[i].code]) {
        return true
      }
      // 不存在该元素，则赋值为true，可以赋任意值，相应的修改if判断条件即可
      hash[arr[i].code] = true
    }
    return false
  }
  // 提交表单
  const dictsubmitForm = () => {
    let isOk = true
    info.dictTableData.forEach((item: any) => {
      if (item.name.length <= 0 || item.code.length <= 0) {
        isOk = false
      }
    })
    if (!isOk) {
      ElMessage({
        showClose: true,
        message: '请完善列表',
        type: 'warning',
      })
      return false
    }

    //对数组进行排序，查上一个和下一个是否相等

    if (isRepeat(info.dictTableData)) {
      ElMessage({
        showClose: true,
        message: '编码重复',
        type: 'error',
      })
      return false
    }
    info.dictsaveLoading = true
    postData(sysApi.dict.saveBatchDataDictUrl, info.dictTableData)
      .then(
        ({ msg }) => {
          info.dictdialogVisible = false
          info.dictsaveLoading = false
          ElMessage({
            showClose: true,
            message: msg,
            type: 'success',
          })
          getDictData()
        },
        () => {
          getDictData()
          info.dictsaveLoading = false
        }
      )
      .catch(() => {
        info.dictsaveLoading = false
      })
  }
  // reloadClick
  const dictreloadClickFn = (row: any) => {
    getDictData()
  }

  // 删除按钮点击
  const removeDictList = (row: any, index: any) => {
    ElMessageBox.confirm('确定删除？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
      .then(() => {
        if (row.id) {
          getData(sysApi.dict.removeDataDictUrl + row.id, {}).then(
            ({ msg }) => {
              ElMessage({
                showClose: true,
                message: msg,
                type: 'success',
              })
              getDictData()
              info.dictTableData.splice(index, 1)
            }
          )
        } else {
          info.dictTableData.splice(index, 1)
        }
      })
      .catch(() => {})
  }

  const addDictList = () => {
    info.dictTableData.push({
      snRowKey: info.dictTableData.length + 1,
      sn: info.dictTableData.length + 1,
      name: '',
      code: '',
      dictType: 'node',
      dictKey: info.dictKey,
      parentId: info.dictrow.id,
      typeCode: info.dictrow.typeCode,
    })
  }
  // 拖拽排序
  // 行拖拽
  const rowDrop = () => {
    setTimeout(() => {
      const table = document.querySelector(
        '.dataDictList .el-table__body-wrapper tbody'
      )
      Sortable.create(table, {
        animation: 300,
        handle: '.move-btn',
        onEnd: (evt: any) => {
          const t = info.dictTableData.splice(evt.oldIndex, 1)[0]
          info.dictTableData.splice(evt.newIndex, 0, t)
          info.dictTableData.forEach((item: any, index: any) => {
            item.sn = index + 1
          })
        },
      })
    })
  }

  onMounted(() => {
    getDataTree()
    listFn()
  })
</script>
