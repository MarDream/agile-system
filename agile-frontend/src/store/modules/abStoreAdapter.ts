/**
 * 平台模块依赖的状态管理，需要自行实现关键接口
 */
// 引入已有功能，如果在自行平台，可引入自身的，或者实现相关接口
import { useRouterMenuStore } from './routerMenusStore'
import { useUserStore } from './userStore'
import { defineStore } from 'pinia'
import router from '@/router'

export const useAbStoreAdapter = defineStore('abStoreAdapter', {
  getters: {
    //获取按钮权限，用于权限控制指令
    getButtonPermission: () => {
      return useUserStore().getButtonPermission
    },
    getUserMenuList: () => {
      // 快捷菜单用的，可以不实现
      return []
    },
    getAbUser: () => {
      return useUserStore().getAbUsers
    },
    getCurrentOrg: () => {
      return useUserStore().getCurrentOrg
    },
  },
  actions: {
    // 修改路由的名字
    changeTabsMeta: (info: TabEditInfo) => {
      const tabsStore = useRouterMenuStore()
      tabsStore.updateTabTitle(info.fullPath, info.meta.title)
    },
    // 关闭指定TAB
    closeTab: (fullPath: string) => {
      const tabsStore = useRouterMenuStore()
      tabsStore.removeTab(fullPath)
    },
    // 处理平台内部的异常
    abErrorHandler: async (data: any) => {
      if (data.code === 'token_invalid') {
        router.replace({ path: '/login' })
      }
    },
  },
})
