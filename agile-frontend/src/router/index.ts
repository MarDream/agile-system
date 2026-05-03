import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import pinia from '@/store'
import { useRouterMenuStore } from '@/store/modules/routerMenusStore'
import { useMainStore } from '@/store/modules/mainStore'

import { IRouteMenu } from '@/models/IRouterMenu'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { ElNotification } from 'element-plus'
import { useDefaultConfig } from '@/config/defaultConfig'
import { checkTokenExp } from '@/utils'
import { useHelper } from '@/store/modules/storeHelper'
import layout from '@/layout/index.vue'
import login from '@/views/login/index.vue'
import home from '@/views/home/index.vue'
const config = useDefaultConfig()
//系统特殊路由
const routes_404 = {
  path: '/:pathMatch(.*)*',
  hidden: true,
  component: () => import('@/layout/other/404.vue'),
}

import Org from './modules/org'
import BPM from './modules/bpm'
import Sys from './modules/sys'
import Cms from './modules/cms'
import Biz from './modules/biz'

const router = createRouter({
  history: createWebHistory(), //createWebHashHistory(),
  routes: [
    Cms,
    Org,
    BPM,
    Sys,
    Biz,
    {
      name: 'layout',
      path: '/',
      component: layout,
      redirect: config.DASHBOARD_URL || '/dashboard',
      children: [
        {
          name: 'dashboard',
          path: '/dashboard',
          meta: {
            title: '控制台',
            icon: 'Menu',
            affix: true,
            type: 'menu',
          },
          component: home,
        },
      ],
    },
    {
      path: '/login',
      component: login,
      meta: {
        title: '登录',
      },
    },
    {
      path: '/formDesigner',
      name: 'FormDesigner',
      component: () => import('@/views/form-designer/index.vue'),
      meta: {
        title: '设计器',
        // 目前咱是未实现路由缓存，等下个版本在搞
        noKeepAlive: true,
      },
    },
    {
      path: '/bpmDesigner',
      name: 'BpmDesigner',
      component: () => import('@/views/bpm/definition/bpmDesign.vue'),
      meta: {
        title: '流程设计器',
        noKeepAlive: true,
      },
    },
    {
      path: '/bizForm/preview/:code',
      name: 'BizFormPreview',
      component: () => import('@/views/biz/bizForm/formPreview.vue'),
      meta: {
        title: '表单预览',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/bpmInstancePrint',
      name: 'BpmInstancePrintHome',
      component: () => import('@/views/bpm/instance/bpmInstancePrint.vue'),
      meta: {
        title: '流程打印',
        noKeepAlive: false,
      },
    },
    {
      path: '/bizForm/bizDetail/:code',
      name: 'bizDetail',
      component: () => import('@/views/biz/bizForm/formPreview.vue'),
      meta: {
        title: '表单详情',
        icon: 'dashboard-line',
        dynamicNewTab: true,
      },
    },
  ],
})
//判断是否已加载过动态/静态路由
const defaultLogin = '/login'
router.beforeEach(async (to, from, next) => {
  NProgress.start()
  document.title = to.meta.title
    ? `${to.meta.title} - ${config.APP_NAME}`
    : `${config.APP_NAME}`
  if (checkTokenExp()) {
    localStorage.clear()
    useHelper.resetStore()
    if (to.path === defaultLogin) {
      next()
    } else {
      ElNotification.error({
        title: '认证失效提示',
        message: '身份认证时间已过期，系统已自动退出，请重新登陆！',
        type: 'error',
      })
      next(defaultLogin)
    }
  } else {
    const routeLength = router.getRoutes().length
    //整页路由处理
    if (to.meta.fullpage) {
      to.matched = [to.matched[to.matched.length - 1]]
    }
    //加载动态/静态路由
    if (routeLength === 3) {
      next({ path: config.DASHBOARD_URL, replace: true })
    } else {
      next()
    }
  }
})

router.afterEach((to, from) => {
  // afterEach(to)
  NProgress.done()
})

router.onError((error) => {
  NProgress.done()
  ElNotification.error({
    title: '路由错误',
    message: error.message,
  })
})

export default router
