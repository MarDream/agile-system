<template>
  <el-container class="layout-container-demo">
    <el-header>
      <router-link
        v-if="type != '1'"
        :to="{
          name: 'ApplicationList',
        }"
      >
        <el-button :icon="Back">返回</el-button>
      </router-link>
      <ab-save
        v-else
        back-name="ApplicationList"
        :form-ref="formRef"
        :save-data="info.data"
        :url="sysApi.authApplication.applicationSaveOrUpdate"
      />
      <ab-load
        v-model="info.data"
        :url="sysApi.authApplication.applicationGet"
      />
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
            label="系统名称"
            prop="name"
            :rules="[{ required: true, message: '必填' }]"
          >
            <el-input
              v-model="info.data.name"
              :disabled="type != '1'"
              placeholder="请输入系统名称"
            />
          </el-form-item>

          <el-form-item
            label="系统编码"
            prop="code"
            :rules="[{ required: true, message: '必填' }, info.validatorMap]"
          >
            <el-input
              v-model="info.data.code"
              :disabled="info.data.id"
              placeholder="请输入系统编码"
            />
          </el-form-item>
          <el-form-item label="应用类型" prop="appType">
            <el-radio-group v-model="info.data.appType">
              <el-radio :label="0" size="large">web端</el-radio>
              <el-radio :label="1" size="large">移动端</el-radio>
              <el-radio :label="2" size="large">第三方应用</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            label="url地址"
            prop="url"
            v-if="info.data.appType == 2"
          >
            <el-input
              v-model="info.data.url"
              :disabled="type != '1'"
              placeholder="请输入系统url地址"
            />
          </el-form-item>

          <el-form-item
            label="回调url地址"
            prop="redirectUri"
            v-if="info.data.appType == 2"
          >
            <el-input
              v-model="info.data.redirectUri"
              :disabled="type != '1'"
              placeholder="请输入回调的url地址"
            />
          </el-form-item>

          <el-form-item
            label="打开方式"
            prop="openType"
            v-if="info.data.appType == 2"
          >
            <el-radio
              v-model="info.data.openType"
              :disabled="type != '1'"
              :label="'0'"
            >
              跳转
            </el-radio>
            <el-radio
              v-model="info.data.openType"
              :disabled="type != '1'"
              :label="'1'"
            >
              重定向
            </el-radio>
          </el-form-item>

          <el-form-item label="密匙" prop="secret">
            <el-input
              v-model="info.data.secret"
              :disabled="type != '1'"
              placeholder="请输入密匙"
            />
          </el-form-item>

          <el-form-item
            label="refresh有效期"
            prop="refreshTokenValidity"
            :rules="[
              { pattern: /^-?[0-9]\d*$/, message: '请勿输入数字以外的字符' },
            ]"
          >
            <el-input
              v-model="info.data.refreshTokenValidity"
              :disabled="type != '1'"
              placeholder="请输入刷新token有效期(单位:秒)"
            />
          </el-form-item>

          <el-form-item
            label="access有效期"
            prop="accessTokenValidity"
            :rules="[
              { pattern: /^-?[0-9]\d*$/, message: '请勿输入数字以外的字符' },
            ]"
          >
            <el-input
              v-model="info.data.accessTokenValidity"
              :disabled="type != '1'"
              placeholder="请输入密匙token有效期(单位:秒)"
            />
          </el-form-item>

          <el-form-item label="可用状态" prop="enabled">
            <el-switch
              v-model="info.data.enabled"
              :active-value="1"
              :disabled="type != '1'"
              :inactive-value="0"
            />
          </el-form-item>

          <el-form-item label="自动授权" prop="autoapprove">
            <el-switch
              v-model="info.data.autoapprove"
              :active-value="1"
              :disabled="type != '1'"
              :inactive-value="0"
            />
          </el-form-item>
          <el-form-item label="是否为默认系统" prop="isDefault">
            <el-switch
              v-model="info.data.isDefault"
              :active-value="1"
              :disabled="type != '1'"
              :inactive-value="0"
            />
          </el-form-item>
          <el-form-item
            label="授权类型"
            placeholder="授权类型"
            prop="grantTypes"
          >
            <el-checkbox-group v-model="info.grantType" :disabled="type != '1'">
              <el-checkbox key="refresh_token" label="refresh_token" />
              <el-checkbox key="password" label="password" />
              <el-checkbox
                key="authorization_code"
                label="authorization_code"
              />
              <el-checkbox key="implicit" label="implicit" />
              <el-checkbox
                key="client_credentials"
                label="client_credentials"
              />
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="授权范围" placeholder="授权范围" prop="scope">
            <el-input
              v-model="info.data.scope"
              :disabled="type != '1'"
              placeholder="请输入授权范围"
            />
          </el-form-item>

          <el-form-item
            label="资源集合"
            placeholder="资源集合"
            prop="resourceIds"
          >
            <el-input
              v-model="info.data.resourceIds"
              :disabled="type != '1'"
              placeholder="请输入资源集合"
            />
          </el-form-item>

          <!-- <el-form-item label="扩展配置" placeholder="扩展配置" prop="config">
            <el-input
              v-model="info.data.config"
              :disabled="type != '1'"
              placeholder="请输入扩展配置"
            />
          </el-form-item> -->

          <el-form-item label="描述信息" prop="desc">
            <el-input
              v-model="info.data.desc"
              :disabled="type != '1'"
              placeholder="请输入描述信息"
              type="textarea"
            />
          </el-form-item>
        </el-form>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>
<!--
<script lang="ts">
  let type = ''
  //获取从哪个路由来的
  export default defineComponent({
    beforeRouteEnter(to, from, next) {
      console.log('from', from)
      if (from.name) {
        type = to.name == 'ApplicationEdit' ? '1' : '0'
        console.log(type)
      }
    },
  })
</script> -->

<script setup lang="ts">
import { getCurrentInstance, reactive, ref } from 'vue'
import { Back } from '@element-plus/icons-vue'
import { sysApi, bizApi } from 'agilebpm'
const formRef = ref()
const info: any = reactive({
  type: '',
  isEdit: false,
  validatorMap: '',
  grantType: [] as any[],
  data: {
    name: '',
    code: '',
    secret: '',
    scope: '', //授权范围
    resourceIds: '', //资源集合
    grantTypes: '', //授权类型
    autoapprove: '', //自动授权
    authorities: '', //权限
    refreshTokenValidity: '',
    accessTokenValidity: '',
    url: '',
    redirectUri: '',
    openType: '',
    enabled: 1,
    desc: '',
    config: '',
    isDefault: 0,
    appType: 0,
  },
})

//通过参数获取详情或编辑的标记(只读/修改)
const type = getCurrentInstance()?.proxy?.$route.query.type

bizApi.bizPattern
  .getAllBizValidator()
  .then((data: any) => (info.validatorMap = data.varirule.rule))

watch(
  () => info.grantType,
  () => (info.data.grantTypes = info.grantType.join(','))
)

watch(
  () => info.data.grantTypes,
  () => (info.grantType = info.data.grantTypes.split(','))
)
</script>
