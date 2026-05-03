import Layout from '@/layout/index.vue'
export default {
  path: '/biz',
  name: 'BIZ',
  component: Layout,
  meta: {
    title: '表单管理',
    icon: 'home-2-line',
    breadcrumbHidden: true,
  },
  children: [
    {
      path: 'custom/customDialogList',
      name: 'CustomDialogList',
      component: () => import('@/views/biz/custom/customDialogList.vue'),
      meta: {
        title: '自定义对话框',
        icon: 'dashboard-line',
        keepAlive: true,
      },
    },
    {
      path: 'custom/customDemo',
      name: 'CustomDemo',
      component: () => import('@/views/biz/custom/customDemo.vue'),
      meta: {
        title: '对话框demo',
        icon: 'dashboard-line',
        keepAlive: true,
      },
    },
    {
      path: 'custom/customDialogEdit',
      name: 'CustomDialogEdit',
      component: () => import('@/views/biz/custom/customDialogEdit.vue'),
      meta: {
        title: '编辑自定义对话框',
        icon: 'dashboard-line',
        keepAlive: true,
      },
    },
    {
      path: 'formCustSql/formCustSqlView/:code',
      name: 'FormCustSqlView',
      component: () => import('@/views/biz/formCustSql/formCustSqlView.vue'),
      meta: {
        title: '自定义列表',
        icon: 'dashboard-line',
        dynamicNewTab: true,
        noKeepAlive: false,
      },
    },
    {
      path: 'formCustSql/formCustSqlPreView/:code',
      name: 'FormCustSqlPreView',
      component: () => import('@/views/biz/formCustSql/formCustSqlPreView.vue'),
      meta: {
        title: '列表预览',
        icon: 'dashboard-line',
        dynamicNewTab: true,
        noKeepAlive: false,
      },
    },
    {
      path: '/biz/bizForm/formViewAdd/:code',
      name: 'FormViewAdd',
      component: () => import('@/views/biz/bizForm/formPreview.vue'),
      meta: {
        title: '表单新增',
        icon: 'dashboard-line',
        dynamicNewTab: true,
      },
    },
    {
      path: '/biz/bizForm/formViewEdit/:code',
      name: 'FormViewEdit',
      component: () => import('@/views/biz/bizForm/formPreview.vue'),
      meta: {
        title: '表单编辑',
        icon: 'dashboard-line',
        dynamicNewTab: true,
      },
    },
    {
      path: '/biz/bizForm/formViewDetail/:code',
      name: 'FormViewDetail',
      component: () => import('@/views/biz/bizForm/formPreview.vue'),
      meta: {
        title: '表单详情',
        icon: 'dashboard-line',
        dynamicNewTab: true,
      },
    },
    {
      path: '/biz/bizForm/bizFormDesignList',
      name: 'BizFormDesignList',
      component: () => import('@/views/biz/bizForm/bizFormDesignList.vue'),
      meta: {
        title: '业务表单设计列表页',
        icon: 'dashboard-line',
      },
    },
    {
      path: 'formCustSql/formCustSqlView/:code',
      name: 'FormCustSqlView',
      component: () => import('@/views/biz/formCustSql/formCustSqlView.vue'),
      meta: {
        title: '自定义列表',
        icon: 'dashboard-line',
        dynamicNewTab: true,
        noKeepAlive: false,
      },
    },   
    {
      path: 'bizFormCombinationView/:code',
      name: 'BizFormCombinationView',
      component: () =>
        import('@/views/biz/bizFormCombination/bizFormCombinationView.vue'),
      meta: {
        title: '组联式表单',
        dynamicNewTab: true,
        icon: 'dashboard-line',
      },
    },
  ],
}
