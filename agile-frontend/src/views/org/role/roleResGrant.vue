<template>
  <el-dialog v-model="roleRes.visible" title="角色资源" width="40%">
    <div class="card-header">
      <span>{{ appInfo.name }}</span>
      <ab-cust-dialog
        dialog-key="sysAppSelector"
        type="primary"
        @ok="confirmAppSelector"
      >
        选择应用
      </ab-cust-dialog>
    </div>
    <el-divider />
    <el-tree
      ref="resTreeRef"
      :check-on-click-node="true"
      :data="resTree.data"
      :default-expanded-keys="resTree.expandKeys"
      empty-text="加载中。。。"
      node-key="id"
      show-checkbox
      style="height: 500px; overflow: auto"
    >
      <template #default="{ data }">
        <span class="custom-tree-node">
          <span>
            <vab-icon v-if="data.icon" :icon="data.icon" />
            {{ data.name }}
          </span>
        </span>
      </template>
    </el-tree>
    <template #footer>
      <span class="dialog-footer">
        <el-space wrap>
          <el-button text type="primary" @click="roleRes.visible = false">
            取消
          </el-button>
          <el-button
            v-ab-btn-rights:resRole_grantRoleResource
            :loading="saveLoading"
            type="primary"
            @click="saveRoleRes"
          >
            保存
          </el-button>
        </el-space>
      </span>
    </template>
  </el-dialog>
</template>
<script type="ts" setup>
  import { defineEmits, reactive, ref, toRefs } from "vue";
  import { sysApi, authApi, abUtil,postData } from 'agilebpm'
  import { ElMessage } from "element-plus";

  const props = defineProps({
    roleRes: { required: true, type: Object },
    roleId: { required: true, type: String }
  });

  const { roleRes,roleId } = toRefs(props)

  const resTreeRef = ref()

  const resTree = reactive({
    data: [],
    setData(data) {
      resTree.data = data;
      // 选中数据项
      setTimeout(() => resTree.setCheckedKeys(), 0)
    }
    ,
    expandKeys: computed(() => {
      return abUtil.expandTwoLevels(resTree.data, 'id');
    }),
    setCheckedKeys() {
      if (resTree.data && resTree.data) {
        const items = [...resTree.data];
        while (items && items.length > 0) {
          const item = items.shift();
          if (item.checked) {
            resTreeRef.value.setChecked(item.id, true, false);
          }
          if (item.children) {
            items.push(...item.children);
          }
        }
      }
    }
  });

  const appInfo = reactive({
    id: "",
    name: "",
    code: ""
  });


  // 保存进度条
  const saveLoading = ref(false);

  /**
   * 刷新角色资源树
   */
  const refreshRoleResTree = () => {
    // 获取资源角色树
    authApi.getRoleResTreeData(roleId.value, appInfo.code)
      .then(res => {
        resTree.setData(res.data);
      });
  };

  watch(roleId, (newValue) => {
    resTree.setData([])
    if (newValue) {
      appInfo.code = 'bpmDevPlatform';
      // 获取默认应用信息
      postData(sysApi.authApplication.applicationListJson, { "queryParam": { "code$VEQ": appInfo.code } })
        .then(res => {
          const { id, name, code } = res.data.rows[0];
          appInfo.id = id;
          appInfo.name = name;
          appInfo.code = code;
        });
      refreshRoleResTree();
    }
  });

  /**
   * 应用选择器确认
   * @param dataList 回显数据列表
   */
  const confirmAppSelector = (dataList) => {
    if (dataList) {
      const { id, code, name } = dataList[0];
      appInfo.id = id;
      appInfo.code = code;
      appInfo.name = name;
      refreshRoleResTree();
    }
  };

  /**
   * 保存角色资源
   */
  const saveRoleRes = () => {
    const resIds = resTreeRef.value.getCheckedKeys()
    const halfResIds = resTreeRef.value.getHalfCheckedKeys()
    authApi.grantRoleResource(roleId.value, appInfo.id, resIds, halfResIds).then(res => {
      ElMessage({
        showClose: true,
        message: "保存成功",
        type: "success"
      });
      saveLoading.value = false;
      roleRes.value.visible = false
    });
  };
</script>
<style lang="scss" scoped>
  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
</style>
