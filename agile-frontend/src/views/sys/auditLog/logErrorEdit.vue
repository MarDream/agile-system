<template>
  <el-container class="layout-container-demo">
    <el-header>
      <ab-load v-model="info.data" :url="sysApi.sysAuditlog.logErrorGet" />
      <ab-save
        back-name="LogErrorList"
        :form-ref="formRef"
        :save-data="info.data"
        :url="sysApi.sysAuditlog.logErrorSave"
      />
    </el-header>
    <el-divider class="dividermar" />
    <el-main>
      <el-scrollbar>
        <div>
          <el-form
            ref="formRef"
            label-suffix="："
            label-width="120px"
            :model="info.data"
            :status-icon="false"
          >
            <el-row>
              <el-col :span="6">
                <el-form-item label="账号" prop="account">
                  <el-input
                    v-model="info.data.account"
                    disabled
                    style="width: 310px"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="18">
                <el-form-item label="IP地址" prop="ip">
                  <el-input
                    v-model="info.data.ip"
                    disabled
                    style="width: 310px"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="状态" prop="status">
                  <el-select v-model="info.data.status" style="width: 310px">
                    <el-option label="未检查" value="unchecked" />
                    <el-option label="已检查" value="checked" />
                    <el-option label="已修复" value="fixed" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="18">
                <el-form-item label="IP归属" prop="ipAddress">
                  <el-input
                    v-model="info.data.ipAddress"
                    disabled
                    style="width: 310px"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="请求地址" prop="url">
              <el-input
                v-model="info.data.url"
                disabled
                style="min-width: 735px"
              />
            </el-form-item>
            <el-form-item label="出错时间" prop="createTime">
              <el-input
                v-model="info.data.createTime"
                disabled
                style="min-width: 735px"
              />
            </el-form-item>
            <el-form-item label="出错信息" prop="content">
              <el-input
                v-model="info.data.content"
                disabled
                rows="2"
                style="min-width: 735px"
                type="textarea"
              />
            </el-form-item>
            <el-form-item label="请求参数" prop="requestParam">
              <el-input
                v-model="info.data.requestParam"
                disabled
                rows="2"
                style="min-width: 735px"
                type="textarea"
              />
            </el-form-item>
            <el-form-item label="请求Head" prop="heads">
              <el-input
                v-model="info.data.heads"
                disabled
                rows="5"
                style="min-width: 80%"
                type="textarea"
              />
            </el-form-item>
            <el-form-item label="堆栈信息" prop="stackTrace">
              <codemirror
                v-model="info.data.stackTrace"
                :autofocus="true"
                :indent-with-tab="true"
                placeholder=""
                :style="{
                  height: `600px`,
                  border: `1px solid #e4e7ed`,
                  width: `80%`,
                }"
              />
            </el-form-item>
          </el-form>
        </div>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue'
  import { sysApi, bizApi } from 'agilebpm'
  import { Codemirror } from 'vue-codemirror'
  const formRef = ref()

  const info: any = reactive({
    arr: [],
    validatorMap: {},
    data: {} as any,
  })

  //获取校验
  bizApi.bizPattern
    .getAllBizValidator()
    .then((data: any) => (info.validatorMap = data.varirule.rule))
</script>
