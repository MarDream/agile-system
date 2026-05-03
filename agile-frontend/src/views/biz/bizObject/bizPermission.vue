<template>
  <span @click="open">
    <slot></slot>
  </span>

  <el-dialog
    v-model="info.dialogVisible"
    append-to-body
    destroy-on-close
    title="权限配置"
    width="70%"
  >
    <el-container
      v-loading="info.loadProgress != 2"
      class="layout-container-demo"
    >
      <el-header>
        <ab-save
          :after-save-fn="afterSaveFn"
          :back-btn="false"
          :form-ref="formRef"
          :save-data="saveData"
          :url="bizApi.bizPermission.BizPermissionSave"
        />
        <el-button :icon="Back" @click="info.dialogVisible = false">
          返回
        </el-button>

        <ab-load
          v-model="info.data"
          :get-param="`?type=${type}&value=${value}`"
          :url="bizApi.bizPermission.BizPermissioGetPermission"
          @after-fn="afterFn"
        />
        <el-button
          :icon="Refresh"
          plain
          style="margin-left: 10px"
          type="warning"
          @click="reset()"
        >
          重置
        </el-button>
      </el-header>
      <el-main>
        <el-descriptions border :column="2">
          <el-descriptions-item label-align="right">
            <template #label>权限对象类型</template>
            <span v-if="info.BizPermissionType">
              {{ info.BizPermissionType[type] }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label-align="right">
            <template #label>权限对象值</template>
            {{ value }}
          </el-descriptions-item>
        </el-descriptions>

        <el-table
          v-if="info.loadProgress == 2"
          border
          :data="info.tableData"
          style="width: 100%; margin-top: 10px"
        >
          <el-table-column label="名称" width="300">
            <template #default="scope">
              <el-tag v-if="scope.row.tableMap" class="ml-2" type="danger">
                业务对象
              </el-tag>
              <el-tag
                v-else-if="scope.row.columnMap"
                class="ml-2"
                type="success"
              >
                业务实体
              </el-tag>
              <el-tag v-else class="ml-2" type="warning">字段</el-tag>
              {{ `${scope.row.comment}（${scope.row.code}）` }}
              <span v-if="scope.row.columnMap" style="float: right">
                <el-button
                  v-if="!scope.row.expanded"
                  circle
                  :icon="ArrowDownBold"
                  plain
                  @click="expand(scope)"
                />
                <el-button
                  v-if="scope.row.expanded"
                  circle
                  :icon="ArrowUpBold"
                  plain
                  @click="unexpand(scope)"
                />
              </span>
            </template>
          </el-table-column>
          <el-table-column
            v-for="item in info.RightsType"
            :key="item.key"
            :label="item.desc"
          >
            <template #default="scope">
              <el-checkbox
                v-if="!getRightsDesc(scope.row.rights[item.key])"
                :disabled="ckDis(scope.row, item.key)"
                :indeterminate="ckInd(scope.row, item.key)"
                :model-value="ck(scope.row, item.key)"
                size="large"
                @change="ckChange(scope.row, item.key, $event)"
              />
              <el-tag
                v-if="!!getRightsDesc(scope.row.rights[item.key])"
                style="margin-left: 5px"
              >
                {{ getRightsDesc(scope.row.rights[item.key]) }}
              </el-tag>
              <span style="float: right; margin-top: 8px">
                <el-button
                  v-if="
                    scope.row.rights[item.key] &&
                    scope.row.rights[item.key].length > 0 &&
                    !ckDis(scope.row, item.key)
                  "
                  circle
                  :icon="Refresh"
                  size="small"
                  @click="scope.row.rights[item.key] = []"
                />
                <multim-user-selector
                  :disabled="ckDis(scope.row, item.key)"
                  :init-data="selectorInit(scope.row.rights[item.key])"
                  style="margin-left: 5px"
                  @ok="selectorOk(scope.row.rights, item.key, $event)"
                >
                  <el-button
                    circle
                    :disabled="ckDis(scope.row, item.key)"
                    :icon="Edit"
                    size="small"
                  />
                </multim-user-selector>
              </span>
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-container>
  </el-dialog>
</template>

<script setup lang="ts">
  import { onMounted, reactive, computed, ref } from 'vue'
  import type { FormInstance } from 'element-plus'
  import { ElMessage } from 'element-plus'
  import {
    Edit,
    Refresh,
    ArrowDownBold,
    ArrowUpBold,
    Back,
  } from '@element-plus/icons-vue'

  import { bizApi, getData, sysApi,abUtil } from 'agilebpm'

  const props = defineProps({
    type: {
      type: String,
      required: true,
    },
    value: {
      type: String,
      required: true,
    },
    boCodes: {
      type: String,
      required: true,
    },
  })

  const info: any = reactive({
    data: {
      type: props.type,
      value: props.value,
      boMap: {},
    }, //数据对象
    tableData: [],
    BizPermissionType: {},
    bizObjects: [],
    RightsType: [
      { key: 'b', desc: '必填' },
      { key: 'w', desc: '编辑' },
      { key: 'r', desc: '只读' },
    ],
    loadProgress: 0, //加载数据进度
    dialogVisible: false,
    types: [
      { key: 'user', val: '用户' },
      { key: 'org', val: '组织' },
      { key: 'role', val: '角色' },
      { key: 'post', val: '岗位' },
    ],
  })
  const formRef = ref<FormInstance>()

  onMounted(() => {})

  const saveData = computed(() => {
    const data = {
      type: props.type,
      value: props.value,
      boMap: {},
    }
    if (info.loadProgress != 2) {
      return data
    }

    info.bizObjects.forEach((b: any) => {
      const bo: any = {}
      bo.code = b.code
      bo.comment = b.name
      bo.rights = info.data.boMap[b.code].rights

      bo.tableMap = {}
      const tables = getAllTables(b.rel)
      tables.forEach((t: any) => {
        const table: any = {}
        table.code = t.code
        table.comment = t.comment
        table.rights = info.data.boMap[b.code].tableMap[t.code].rights

        table.columnMap = {}
        t.columnsWithoutPrimary.forEach((c: any) => {
          const column: any = {}
          column.code = c.code
          column.comment = c.comment
          column.rights =
            info.data.boMap[b.code].tableMap[t.code].columnMap[c.code].rights

          table.columnMap[column.code] = column
        })

        bo.tableMap[table.code] = table
      })
      data.boMap[bo.code] = bo
    })
    return data
  })

  const handleBoMap = (boMap: any) => {
    info.bizObjects.forEach((b: any) => {
      const bo: any = {}
      bo.code = b.code
      bo.comment = b.name
      //业务对象默认编辑权限
      bo.rights = {}
      setRights(bo.rights, 'w', 'everyone')
      setRights(bo.rights, 'b', 'none')
      setRights(bo.rights, 'r', 'none')

      if (boMap && boMap[b.code]) {
        bo.rights = boMap[b.code].rights
      }

      bo.tableMap = {}

      const tables = getAllTables(b.rel)
      tables.forEach((t: any) => {
        const table: any = {}
        table.code = t.code
        table.comment = t.comment
        table.rights = {}
        if (boMap && boMap[b.code] && boMap[b.code].tableMap[t.code]) {
          table.rights = boMap[b.code].tableMap[t.code].rights
        }

        table.columnMap = {}
        t.columnsWithoutPrimary.forEach((c: any) => {
          const column: any = {}
          column.code = c.code
          column.comment = c.comment
          column.rights = {}
          if (
            boMap &&
            boMap[b.code] &&
            boMap[b.code].tableMap[t.code] &&
            boMap[b.code].tableMap[t.code].columnMap[c.code]
          ) {
            column.rights =
              boMap[b.code].tableMap[t.code].columnMap[c.code].rights
          }
          table.columnMap[column.code] = column
          column.parent = table
        })
        bo.tableMap[table.code] = table
        table.parent = bo
      })

      info.data.boMap[bo.code] = bo
    })
    initTableData()
    console.info(info.data.boMap)
  }

  const initTableData = () => {
    info.tableData = []
    props.boCodes.split(',').forEach((b: any) => {
      const bo = info.data.boMap[b]
      info.tableData.push(bo)
      Object.values(bo.tableMap).forEach((t: any) => {
        info.tableData.push(t)
      })
    })
  }

  const getAllTables = (rel: any) => {
    const tables = []
    tables.push(rel.table)
    rel.children.forEach((r: any) => {
      const ts = getAllTables(r)
      ts.forEach((t: any) => {
        tables.push(t)
      })
    })
    return tables
  }

  const expand = (scope: any) => {
    scope.row.expanded = true
    if (scope.row.columnMap) {
      Object.values(scope.row.columnMap).forEach((c) => {
        info.tableData.splice(scope.$index + 1, 0, c)
      })
    }
  }

  const unexpand = (scope: any) => {
    scope.row.expanded = false
    if (scope.row.columnMap) {
      Object.values(scope.row.columnMap).forEach((c) => {
        abUtil.Arrays.remove(c, info.tableData)
      })
    }
  }

  const setRights = (rights: any, key: string, type: string) => {
    rights[key] = [
      {
        type: type,
      },
    ]
    if (type === 'everyone') {
      rights[key][0].desc = '所有人'
    } else {
      rights[key][0].desc = '无权限'
    }
  }

  const getRightsDesc = (rights: any) => {
    if (
      !rights ||
      rights.length == 0 ||
      ['everyone', 'none'].includes(rights[0].type)
    ) {
      return ''
    }

    const list: any = []
    rights.forEach((r: any) => {
      list.push(r.desc)
    })

    return list.join(' 和 ')
  }

  const reset = () => {
    handleBoMap(null)
  }

  const beforeSaveFn = () => {
    delete info.data.config
  }

  const afterFn = (data: any) => {
    info.loadProgress++

    if (info.loadProgress == 2) {
      if (!data) {
        handleBoMap(null)
      } else {
        handleBoMap(info.data.boMap)
      }
    }
  }

  const open = () => {
    info.loadProgress = 0
    info.dialogVisible = true
    info.data.boMap = {}

    sysApi.tools
      .getEnum('com.dstz.biz.api.constant.BizPermissionType', true)
      .then((resp: { data: any[] }) => {
        resp.data.forEach((item: any) => {
          info.BizPermissionType[item.key] = item.desc
        })
        console.info(info.BizPermissionType)
      })

    getData(bizApi.bizObject.BizObjectGets, {
      code: props.boCodes,
    }).then((resp: any) => {
      info.bizObjects = resp.data
      info.loadProgress++
      if (info.loadProgress == 2) {
        handleBoMap(info.data.boMap)
      }
    })
  }

  const afterSaveFn = () => {
    ElMessage({
      showClose: true,
      message: '保存成功',
      type: 'success',
    })
    return false
  }

  const ck = (row: any, key: string) => {
    //所有人才显示选中
    if (
      row.rights[key] &&
      row.rights[key].length > 0 &&
      row.rights[key][0].type == 'everyone'
    ) {
      return true
    }

    return false
  }

  const ckChange = (row: any, key: string, b: any) => {
    if (b) {
      setRights(row.rights, key, 'everyone')
    } else {
      setRights(row.rights, key, 'none')
    }
  }

  const ckInd = (row: any, key: string) => {
    //自身存在任意一种配置则就不会有继承状态
    for (let i = 0; i < 3; i++) {
      const k = ['r', 'w', 'b'][i]
      if (row.rights[k] && row.rights[k].length > 0) {
        return false
      }
    }

    const parent = row.parent
    //父存在配置
    if (parent && parent.rights[key] && parent.rights[key].length > 0) {
      //父配置不是“无”则继承
      if (parent.rights[key][0].type != 'none') {
        return true
      } else {
        //父配置的是“无”则不继承
        return false
      }
    }

    const p = parent.parent
    //父不存在配置直接越级继承
    if (
      p &&
      p.rights[key] &&
      p.rights[key].length > 0 &&
      p.rights[key][0].type != 'none'
    ) {
      return true
    }

    return false
  }

  const ckDis = (row: any, key: string) => {
    const keys = ['b', 'w', 'r']

    for (let i = 0; i < keys.indexOf(key); i++) {
      const k = keys[i]
      //如果配置了左侧权限
      if (row.rights[k] && row.rights[k].length > 0) {
        if (row.rights[k][0].type != 'none') {
          //且不为“无”则当前只读
          return true
        } else if (keys[i + 1] == key) {
          return false
        }
      }
    }

    //如果存在父则继承父的只读状态
    if (row.parent && ckDis(row.parent, key)) {
      return true
    }

    return false
  }

  const selectorOk = (rights: any, key: string, data: any) => {
    rights[key] = []

    info.types.forEach((type: any) => {
      if (data[type.key].length > 0) {
        rights[key].push({
          type: type.key,
          desc: `（${type.val}）【${data[type.key]
            .map((item: any) => item.name)
            .join('、')}】`,
          data: data[type.key],
        })
      }
    })
  }

  const selectorInit = (list: any) => {
    if (!list) {
      return {}
    }
    const data = {}
    list.forEach((item: any) => {
      info.types.forEach((type: any) => {
        if (item.type == type.key) {
          data[type.key] = item.data
        }
      })
    })

    return data
  }
</script>
