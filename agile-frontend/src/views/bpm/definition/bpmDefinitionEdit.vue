<template>
  <el-container class="layout-container-demo">
    <el-header height="80px">
      <ab-save
        :after-save-fn="saveAfter"
        back-name="BpmDefinitionList"
        :form-ref="formRef"
        :save-data="data.bpmDefinition"
        :url="bpmApi.bpmDefinition.bpmDefinitionSaveUrl"
      />
      <ab-load
        v-model="data.bpmDefinition"
        :get-param="id"
        :url="bpmApi.bpmDefinition.bpmDefinitionGetUrl"
      />
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-form
        ref="formRef"
        label-suffix="："
        label-width="120px"
        :model="data.bpmDefinition"
        :status-icon="false"
      >
        <el-form-item
          label="流程名称"
          prop="name"
          :rules="[
            { required: true, message: '必填' },
            { max: 50, message: '最多可输入50个字符' },
          ]"
        >
          <ab-pinyin
            v-model="data.bpmDefinition.name"
            v-model:copy="data.bpmDefinition.desc"
            v-model:to="data.bpmDefinition.key"
          />
        </el-form-item>
        <el-form-item
          label="流程编码"
          prop="key"
          :rules="[
            { required: true, message: '必填' },
            { max: 50, message: '最多可输入50个字符' },
            {
              pattern: /^[a-zA-Z]\w*$/,
              message: '只能以字母开头,允许字母、数字和下划线',
            },
          ]"
        >
          <ab-code
            v-model="data.bpmDefinition.key"
            :disabled="data.bpmDefinition.id"
          />
        </el-form-item>
        <el-form-item
          label="分类"
          prop="typeCode"
          :rules="[{ required: true, message: '必填' }]"
        >
          <ab-select-tree
            v-model="data.bpmDefinition.typeCode"
            type-code="flowType"
          />
        </el-form-item>
        <el-form-item
          label="描述"
          prop="desc"
          :rules="[
            { required: true, message: '必填' },
            { max: 500, message: '最多可输入500个字符' },
          ]"
        >
          <el-input v-model="data.bpmDefinition.desc" type="textarea" />
        </el-form-item>
        <el-form-item label="排序" prop="sn">
          <el-input v-model="data.bpmDefinition.sn" type="number" />
        </el-form-item>
        <el-form-item label="ICON" prop="iconStyle">
          <ab-choose-svg v-model="data.bpmDefinition.iconStyle" />
        </el-form-item>
        <el-form-item label="是否隐藏" prop="hidden">
          <el-tooltip
            content="隐藏后将不在个人办公-发起申请页面展示，但依然可以通过其他方式发起流程"
          >
            <el-switch
              v-model="data.bpmDefinition.hidden"
              active-text="是"
              :active-value="1"
              inactive-text="否"
              :inactive-value="0"
              inline-prompt
            />
          </el-tooltip>
        </el-form-item>
      </el-form>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue'
  import type { FormInstance } from 'element-plus'
  import { bpmApi, abUtil, abTools } from 'agilebpm'
  import { abChooseSvg } from 'agilebpm'

  //引入 当前实例
  const { proxy } = abTools.useCurrentInstance()
  const formRef = ref<FormInstance>()
  const id = proxy.$route.query.id
  const data = reactive({
    bpmDefinition: {
      name: '',
      key: '',
      desc: '',
      sn: '10',
      iconStyle: '',
      typeCode: '',
      id: null as any,
      processEditor: 'empty',
      hidden: 0,
    },
  })

  const saveAfter = (data: any) => {
    if (!id) {
      proxy.$router.push({
        name: 'BpmDefinitionList',
        query: { newDefinitionId: data },
      })
    } else {
      proxy.$router.push({ name: 'BpmDefinitionList' })
    }

    return false
  }

  onMounted(() => {
    data.bpmDefinition.typeCode = proxy.$route.query.typeCode
  })
</script>
