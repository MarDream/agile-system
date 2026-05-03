<template>
  <div class="resourceTree">
    <p class="selectvalue">{{state.selectvalue}}</p>
    <el-card
      class="box-card treeBoxHeight"
      shadow="hover"
      style="margin: 0 12px"
      :style="{ height: state.height }"
    >
      <el-tree
        :allow-drop="allowDrop"
        :current-node-key="state.nodekey"
        :data="state.data"
        :default-expanded-keys="state.defaultExpanded"
        draggable
        empty-text="加载中。。。"
        :expand-on-click-node="false"
        :highlight-current="true"
        node-key="id"
        @node-click="handleNodeClick"
        @node-drop="nodeDrop"
      >
        <template #default="{ data }">
          <div class="custom-tree-node">
            <el-popover placement="right-end" trigger="hover" :width="100">
              <template #reference>
                <p>
                  <vab-icon :icon="data.icon" />
                  {{ data.name }}
                </p>
              </template>
              <span>
                <a style="cursor: pointer" @click.stop="append(data, true)">
                  添加
                </a>
                <a
                  style="margin-left: 10px; cursor: pointer"
                  @click.stop="removeFn(data)"
                >
                  删除
                </a>
              </span>
            </el-popover>
          </div>
        </template>
      </el-tree>
    </el-card>
  </div>
</template>

<script setup>
  import { onMounted, reactive, toRefs } from 'vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import {
    sysApi,
    abTools,
    abUtil,
    postData,
    registerApi,
    authApi,
  } from 'agilebpm'

  const emit = defineEmits([
    'updateFetchdata',
    'updateLoading',
    'addFn',
    'resetFields',
    'enableAppFn',
    'updateApiButtonData',
  ])

  const state = reactive({
    defaultExpanded: [],
    nodekey: '',
    height: `${window.innerHeight - (110 + 32 + 20 + 55 + 50)}px`,
    selectvalue: null,
    selectoption: [],
    data: [],
    datato: {
      systemId: null,
    },
    isshow: false,
    id: null,
    enableApp: 0,
    allTreeData: [],
  })

  // 选择流程平台 / 后台
  const changeselectvalue = (value) => {
    state.datato.systemId = value
    state.data = []
    fetchTreeData()
    state.selectoption.forEach((item, index) => {
      if (item.id === value) {
        state.enableApp = item.enableApp
      }
    })
    emit('enableAppFn', state.enableApp)
  }

  // 添加子级/编辑打开窗口
  const append = (row) => {
    emit('addFn', row)
  }
  // 点击删除
  const removeFn = (row) => {
    ElMessageBox.confirm(`确定删除【${row.name}】这个菜单吗`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(() => {
      authApi.removeResource({ id: row.id }).then(({ msg }) => {
        ElMessage({
          type: 'success',
          message: '已删除',
          onClose: () => {},
        })
        fetchTreeData()
        emit('resetFields')
        registerApi.getUserInfo().then((dataInfo) => {
           
        })
        // removereset(row.parentId)
      })
    })
  }

  // 请求详情信息
  const fetchData = async (id) => {
    emit('updateLoading', true)
    const data = await authApi.getJson({ id: id })
    emit('updateLoading', false)
    emit('updateFetchdata', data.data)
    const apiButtonData = getTreeName(state.allTreeData, id)
    let arr = []
    if (apiButtonData.children && apiButtonData.children.length > 0) {
      arr = apiButtonData.children.filter((item) => {
        return item.type !== 'menu'
      })
    }
    emit('updateApiButtonData', arr)
  }

  // 点击单个tree属性菜单
  const handleNodeClick = ({ id }) => {
    if (id !== '0') {
      state.id = id
      fetchData(id)
    } else {
      return
    }
  }

  // 根据树结构节点id从树结构数据中获取节点数据
  const getTreeName = (treeList, id) => {
    for (let i = 0; i < treeList.length; i++) {
      const treeItem = treeList[i]
      if (treeItem.id === id) {
        return treeItem
      } else {
        if (treeItem.children && treeItem.children.length > 0) {
          const res = getTreeName(treeItem.children, id)
          if (res) {
            return res
          }
        }
      }
    }
  }

  const getapplicationListJson = () => {
    postData(sysApi.authApplication.applicationListJson, {
      limit: 99,
    }).then(
      (res) => {
        state.selectoption = res.data.rows
        if (state.selectoption && state.selectoption.length > 0) {
          state.selectoption.forEach((item, index) => {
            // isDefault 默认  enabled 启用
            if (item.isDefault === 1 && item.enabled === 1) {
              state.selectvalue = item.name
              state.datato.systemId = item.id
              state.enableApp = item.enableApp
              emit('enableAppFn', state.enableApp)
            }
          })
          fetchTreeData()
        } else {
          ElMessage({
            type: 'error',
            message: '应用列表为空，无法查询资源树',
            onClose: () => {},
          })
        }
      },
      () => {}
    )
  }

  // 定义递归方法，接收一个数组
  const deepCopy = (params) => {
    // 如果不是对象则退出（可停止递归）
    if (typeof params !== 'object') return
    // 深拷贝初始值：对象/数组
    const newObj = params instanceof Array ? [] : {}
    // 使用 for-in 循环对象属性（包括原型链上的属性）
    for (const i in params) {
      // 只访问对象自身属性
      if (params.hasOwnProperty(i)) {
        // 当前属性还未存在于新对象中时
        if (!(i in newObj)) {
          if (params[i] instanceof Date) {
            // 判断日期类型
            newObj[i] = new Date(params[i].getTime())
          } else if (params[i] instanceof RegExp) {
            // 判断正则类型
            newObj[i] = new RegExp(params[i])
          } else if (
            typeof params[i] === 'object' &&
            params[i].nodeType === 1
          ) {
            // 判断 DOM 元素节点
            const domEle = document.getElementsByTagName(params[i].nodeName)[0]
            newObj[i] = domEle.cloneNode(true)
          } else {
            // 当元素属于对象（排除 Date、RegExp、DOM）类型时递归拷贝
            newObj[i] =
              typeof params[i] === 'object' ? deepCopy(params[i]) : params[i]
          }
        }
      }
    }
    return newObj
  }

  // 递归过滤得到每一项的hidden为false的数据
  function filterData3(arr) {
    return arr
      .filter((item) => {
        return !item.type || item.type == 'menu'
      })
      .map((i) => {
        i = deepCopy(i)
        if (i.children) {
          i.children = filterData3(i.children)
        }
        return i
      })
  }

  // 请求tree菜单列表
  const fetchTreeData = async () => {
    const data = await authApi.getTreeData(state.datato)

    state.data = filterData3(data.data)
    state.allTreeData = data.data
    // 默认展开两级
    state.defaultExpanded = abUtil.expandTwoLevels(state.data, 'id')
  }

  const fetchTreeDataAndData = (id) => {
    authApi.getTreeData(state.datato).then(
      (data) => {
        state.data = filterData3(data.data)
        state.allTreeData = data.data
        // 默认展开两级
        state.defaultExpanded = abUtil.expandTwoLevels(state.data, 'id')
        fetchData(id)
      },
      () => {}
    )
  }
  // 删除后默认选中
  const removereset = (id) => {
    fetchData(id)
  }

  /**
   * 根据树子节点ID查找所有父节点ID
   * @param {array} dataSource 树形结构数据源
   * @param {number} nodeId 子节点ID
   * @returns {array} 包含所有父节点ID的数组，按照从根节点到直接父节点的顺序排序
   */
  function findParentIds(dataSource, nodeId) {
    const parentIds = [] // 用于存储所有父节点ID的数组

    // 定义一个递归函数，用于遍历整棵树并查找子节点的所有父节点
    function traverse(node, nodeId) {
      if (node.id === nodeId) {
        // 如果当前节点的ID等于子节点的ID，则表示已经找到了子节点，可以开始向上查找父节点
        return true // 返回true表示已经找到了子节点
      }

      if (node.children) {
        // 如果当前节点有子节点，则继续遍历子节点
        for (const childNode of node.children) {
          if (traverse(childNode, nodeId)) {
            // 如果在子节点中找到了子节点的父节点，则将当前节点的ID添加到父节点ID数组中，并返回true表示已经找到了子节点
            parentIds.push(node)
            return true
          }
        }
      }

      return false // 如果当前节点不是子节点的父节点，则返回false
    }

    // 从根节点开始遍历整棵树，并调用递归函数查找子节点的所有父节点
    for (const node of dataSource) {
      if (traverse(node, nodeId)) {
        // 如果在当前节点的子树中找到了子节点的父节点，则直接退出循环
        break
      }
    }

    return parentIds // 返回所有父节点ID的数组
  }

  // 拖拽树形
  const nodeDrop = (draggingNode, dropNode, dropType, ev) => {
    let obj = {}
    if (dropNode.data.parentId !== '0') {
      // 拉入子节点
      if (dropType === 'inner') {
        draggingNode.data.parentId = dropNode.data.id
      }
      obj = findParentIds(state.data, dropNode.data.id)[0]
    } else {
      obj = state.data[0]
    }
    ElMessageBox.confirm(
      `确定需要将【${draggingNode.data.name}】移动到【${
        dropNode.data.name
      }】的${
        dropType == 'after'
          ? '后一个节点?'
          : dropType == 'before'
          ? '前一个节点?'
          : dropType == 'inner'
          ? '子节点中?'
          : ''
      }`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
      .then(() => {
        authApi.saveTreeUrl(obj).then(
          (res) => {
            ElNotification({
              title: '提示',
              message: '更换位置成功',
              type: 'success',
            })
            fetchTreeData()
          },
          () => {
            fetchTreeData()
          }
        )
        // postData(
        //   // dropNode.data.parentId !== '0'
        //   //   ? authApi.saveUrl
        //   //   : authApi.saveTreeUrl,
        //   authApi.saveTreeUrl,
        //   obj
        // ).then(
        //   (res) => {
        //     ElNotification({
        //       title: '提示',
        //       message: '更换位置成功',
        //       type: 'success',
        //     })
        //     fetchTreeData()
        //   },
        //   () => {
        //     fetchTreeData()
        //   }
        // )
      })
      .catch(() => {
        fetchTreeData()
      })
  }

  const allowDrop = (draggingNode, dropNode, type) => {
    return true
  }
  // tree菜单列表初始化
  onMounted(() => {
    getapplicationListJson()
  })
  defineExpose({
    ...toRefs(state),
    handleNodeClick,
    fetchTreeData,
    removereset,
    fetchData,
    fetchTreeDataAndData,
  })
</script>

<style lang="scss">
  .selectstyle {
    width: 100%;
    margin: 20px 0;
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
  .treeBoxHeight {
    overflow: auto;
    .el-card__body {
      padding: 8px !important;
    }
  }
  .resourceTree{
    .selectvalue{
      padding:16px;
      font-size:15px;
    }
    .box-card{
      width:auto;
    }
  }
</style>
