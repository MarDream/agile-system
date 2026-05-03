<template>
  <el-container class="layout-container-demo">
    <el-header>
      <ab-save
        back-name="RoleList"
        :form-ref="formRef"
        :save-data="info.data"
        :url="orgApi.role.orgRoleSave"
      />
      <ab-load v-model="info.data" :url="orgApi.role.orgRoleGetUrl" />
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-scrollbar>
        <el-form
          ref="formRef"
          label-suffix="："
          label-width="120px"
          :model="info.data"
          :status-icon="false"
        >
          <el-form-item
            label="分组"
            prop="typeCode"
            :rules="[{ required: true, message: '必填' }]"
          >
            <ab-select-tree
              v-model="info.data.typeCode"
              :operation="false"
              type-code="jsfl"
            />
          </el-form-item>
          <el-form-item
            label="名称"
            prop="name"
            :rules="[{ required: true, message: '必填' }]"
          >
            <ab-pinyin v-model="info.data.name" v-model:to="info.data.code" />
          </el-form-item>
          <el-form-item
            label="编码"
            prop="code"
            :rules="[{ required: true, message: '必填' }]"
          >
            <el-input v-model="info.data.code" />
          </el-form-item>
          <el-form-item label="状态" prop="enabled">
            <el-switch
              v-model="info.data.enabled"
              active-text="启用"
              :active-value="1"
              inactive-text="禁用"
              :inactive-value="0"
              :rules="[{ required: true, message: '必填' }]"
            />
          </el-form-item>
          <el-form-item label="级别" prop="level">
            <el-input v-model.number="info.data.level" type="number" />
          </el-form-item>
          <el-form-item label="描述" prop="desc">
            <el-input
              v-model="info.data.desc"
              :autosize="{ minRows: 2, maxRows: 5 }"
              placeholder="描述"
              type="textarea"
            />
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue'
  import { orgApi, abTools } from 'agilebpm'

  const formRef = ref()
  const { proxy } = abTools.useCurrentInstance()

  const info: any = reactive({
    data: {
      typeCode: '',
      name: '',
      code: '',
      level: 0,
      enabled: 1,
      desc: '',
    },
  })

  onMounted(() => {
    info.data.typeCode = proxy.$route?.query?.typeCode
  })
</script>
<style scoped></style>
