<template>
  <el-container class="layout-container-demo">
    <el-header>
      <ab-save
        back-name="ScriptList"
        :form-ref="formRef"
        :save-data="info.data"
        :url="sysApi.script.scriptSave"
      />
      <ab-load v-model="info.data" :url="sysApi.script.scriptGet" />
      <el-button
        v-ab-btn-rights:script_test
        style="margin-left: 20px"
        type="primary"
        @click="scriptTest(info.data.name, info.data.script)"
      >
        脚本测试
      </el-button>
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-scrollbar>
        <el-form
          ref="formRef"
          label-suffix="："
          :model="info.data"
          :status-icon="false"
        >
          <el-form-item
            label="名称"
            label-width="92px"
            placeholder="请输入名称"
            prop="name"
            :rules="[{ required: true, message: '必填' }]"
          >
            <el-input v-model="info.data.name" />
          </el-form-item>
          <el-form-item
            label="脚本"
            label-width="92px"
            placeholder="请输入脚本"
            prop="script"
            :rules="[{ required: true, message: '必填' }]"
          >
            <codemirror
              v-model="info.data.script"
              :autofocus="true"
              :indent-with-tab="true"
              placeholder="请输入groovy代码（类java语言）"
              :style="{
                height: `400px`,
                border: `1px solid #e4e7ed`,
                width: `500px`,
              }"
            />
          </el-form-item>
          <el-form-item
            label="脚本分类"
            label-width="92px"
            prop="typeCode"
            :rules="[{ required: true, message: '必填' }]"
          >
            <ab-select-tree v-model="info.data.typeCode" type-code="script" />
          </el-form-item>
          <el-form-item
            label="备注"
            label-width="92px"
            placeholder="请输入备注"
            prop="desc"
            :rules="[{ required: true, message: '必填' }]"
          >
            <el-input v-model="info.data.desc" type="textarea" />
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue'
  import { sysApi, abTools } from 'agilebpm'
  import { Codemirror } from 'vue-codemirror'
  import { ElMessage } from 'element-plus'
  const formRef = ref()
  const info: any = reactive({
    data: {
      id: '',
      name: '',
      script: '',
      typeCode: '',
      value: '',
      desc: '',
    },
  })

  const scriptTest = (name: string, script: string) => {
    if (!script || script === '') {
      ElMessage.warning('请输入脚本')
      return
    }
    sysApi.script.scriptTest(name, script).then((result: any) => {
      ElMessage({
        message: result.data,
        type: 'success',
        offset: 300,
      })
    })
  }
  const { proxy } = abTools.useCurrentInstance()
  onMounted(() => {
    info.data.typeCode = proxy.$route.query.typeCode
  })
</script>
