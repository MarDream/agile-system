<template>
  <el-container class="layout-container-demo">
    <el-header>
      <ab-save
        back-name="NoticeType"
        :form-ref="formRef"
        :save-data="info.data"
        :url="cmsNotifyTypeSaveUrl"
      />
      <ab-load v-model="info.data" :url="cmsNotifyTypeGetUrl" />
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
            label="名称"
            placeholder="请输入名称"
            prop="typeName"
            :rules="[{ required: true, message: '必填' }]"
          >
            <el-input v-model="info.data.typeName" />
          </el-form-item>
          <el-form-item label="备注" prop="remark">
            <el-input v-model="info.data.remark" :rows="4" type="textarea" />
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
  const formRef = ref()
  const info: any = reactive({
    data: {
      typeName: '',
      remark: '',
    },
  })
</script>
