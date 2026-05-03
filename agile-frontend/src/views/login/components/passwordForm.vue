<template>
  <el-form
    ref="loginForm"
    :model="form"
    :rules="rules"
    label-width="0"
    size="large"
  >
    <el-form-item prop="user">
      <el-input
        v-model="form.user"
        prefix-icon="el-icon-user"
        clearable
        :placeholder="$t('login.userPlaceholder')"
      ></el-input>
    </el-form-item>
    <el-form-item prop="password">
      <el-input
        v-model="form.password"
        prefix-icon="el-icon-lock"
        clearable
        show-password
        :placeholder="$t('login.PWPlaceholder')"
      ></el-input>
    </el-form-item>
    <!-- <el-form-item style="margin-bottom: 10px;">
      <el-col :span="12">
        <el-checkbox :label="$t('login.rememberMe')" v-model="form.autologin"></el-checkbox>
      </el-col> -->
    <!-- <el-col :span="12" class="login-forgot">
        <router-link to="/reset_password">{{ $t('login.forgetPassword') }}？</router-link>
      </el-col> 
    </el-form-item>-->
    <el-form-item>
      <el-button
        type="primary"
        style="width: 100%"
        :loading="islogin"
        round
        @click="login"
      >
        {{ $t('login.signIn') }}
      </el-button>
    </el-form-item>
    <!-- <div class="login-reg">
      {{ $t('login.noAccount') }}
      <router-link to="/user_register">{{ $t('login.createAccount') }}</router-link>
    </div> -->
  </el-form>
</template>

<script setup lang="ts">
  import { defineComponent, reactive, ref } from 'vue'
  import i18n from '@/locales'
  import { ElMessage, FormInstance } from 'element-plus'
  import { useRouter } from 'vue-router'
  import { useMainStore } from '@/store/modules/mainStore'
  import { useUserStore } from '@/store/modules/userStore'
  import { useRouterMenuStore } from '@/store/modules/routerMenusStore'
  import { registerApi } from 'agilebpm'
  import {
    buildLoginPayload,
    buildLoginSuccessState,
    extractAccessToken,
    resolveViewComponent,
  } from './loginFlow.mjs'
  const routeStore = useRouterMenuStore()
  defineComponent({ name: 'passwordForm' })
  const router = useRouter()
  const loginForm = ref<FormInstance>()
  const userType = ref<string>('admin')
  const mainStore = useMainStore()
  const useStore = useUserStore()
  const form = reactive({
    user: 'admin',
    password: '1',
    autologin: false,
  })
  const rules = reactive({
    user: [
      {
        required: true,
        message: i18n.global.t('login.userError'),
        trigger: 'blur',
      },
    ],
    password: [
      {
        required: true,
        message: i18n.global.t('login.PWError'),
        trigger: 'blur',
      },
    ],
  })
  const islogin = ref<boolean>(false)
  const modules = import.meta.glob('/src/views/**/*.vue')

  function loadComponent(component: string) {
    const viewComponent = resolveViewComponent(component, modules)
    if (viewComponent) {
      return viewComponent
    }
    return () => import('@/layout/other/empty.vue')
  }

  const login = async () => {
    const valid = await loginForm.value
      ?.validate()
      .then(() => true)
      .catch(() => false)
    if (!valid) {
      return
    }

    islogin.value = true

    try {
      const res: any = await registerApi.login(buildLoginPayload(form))
      const accessToken = extractAccessToken(res)

      if (!accessToken) {
        throw new Error('登录成功但未返回访问令牌')
      }

      localStorage.setItem('ab-token', accessToken)
      mainStore.setToken({
        access_token: accessToken,
        create_time: Date.now(),
      })

      const { data } = await registerApi.getUserInfo()
      const loginState = buildLoginSuccessState(res, data, loadComponent)

      routeStore.setMenus(loginState.menuList)
      routeStore.setPlatMenus(loginState.platformMenus)
      useStore.setUsers(loginState.user)
      useStore.setAbUsers(loginState.abUser)
      useStore.setCurrentOrg(loginState.currentOrg)
      useStore.setButtonPermission(loginState.buttonPermission)

      router.push({ path: loginState.redirectPath })
      ElMessage.success('登录成功')
    } catch (error: any) {
      mainStore.clearToken()
      localStorage.removeItem('ab-token')
      ElMessage.error(error?.message || '登录失败')
    } finally {
      islogin.value = false
    }
  }
</script>

<style></style>
