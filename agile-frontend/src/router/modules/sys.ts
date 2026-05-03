import Layout from '@/layout/index.vue'
export default {
  path: '/sys',
  name: 'System',
  component: Layout,
  meta: {
    title: '系统管理',
    icon: 'home-2-line',
    breadcrumbHidden: true,
  },
  children: [
    {
      path: '/sys/dataDic/datadicList',
      name: 'DatadicList',
      component: () => import('@/views/sys/dataDic/datadicList.vue'),
      meta: {
        title: '数据字典',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/sysResourceList',
      name: 'SysResource',
      component: () => import('@/views/sys/sysResourceList/index.vue'),
      meta: {
        title: '资源管理',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/dailyPhrases/dailyPhrasesList',
      name: 'DailyPhrasesList',
      component: () => import('@/views/sys/dailyPhrases/dailyPhrasesList.vue'),
      meta: {
        title: '常用语管理',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/dailyPhrases/dailyPhrasesEdit',
      name: 'DailyPhrasesEdit',
      component: () => import('@/views/sys/dailyPhrases/dailyPhrasesEdit.vue'),
      meta: {
        title: '编辑常用语',
        icon: 'dashboard-line',
      },
    },
     
   /*  {
      path: '/sys/dataSource/dataSourceList',
      name: 'DataSourceList',
      component: () => import('@/views/sys/dataSource/dataSourceList.vue'),
      meta: {
        title: '数据源列表',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/dataSource/dataSourceEdit',
      name: 'DataSourceEdit',
      component: () => import('@/views/sys/dataSource/dataSourceEdit.vue'),
      meta: {
        title: '数据源编辑',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/dataSource/dataSourceDefList',
      name: 'DataSourceDefList',
      component: () => import('@/views/sys/dataSource/dataSourceDefList.vue'),
      meta: {
        title: '数据源模板列表',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/dataSource/dataSourceDefEdit',
      name: 'DataSourceDefEdit',
      component: () => import('@/views/sys/dataSource/dataSourceDefEdit.vue'),
      meta: {
        title: '数据源模板编辑',
        icon: 'dashboard-line',
      },
    }, */
    {
      path: '/sys/auditLog/logErrorList',
      name: 'LogErrorList',
      component: () => import('@/views/sys/auditLog/logErrorList.vue'),
      meta: {
        title: '异常日志列表',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/auditLog/logErrorEdit',
      name: 'LogErrorEdit',
      component: () => import('@/views/sys/auditLog/logErrorEdit.vue'),
      meta: {
        title: '异常日志编辑',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/properties/propertyList',
      name: 'PropertyList',
      component: () => import('@/views/sys/properties/propertyList.vue'),
      meta: {
        title: '系统属性列表',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/properties/propertyEdit',
      name: 'PropertyEdit',
      component: () => import('@/views/sys/properties/propertyEdit.vue'),
      meta: {
        title: '系统属性编辑',
        icon: 'dashboard-line',
      },
    },
    {
      path: '/sys/scripts/scriptList',
      name: 'ScriptList',
      component: () => import('@/views/sys/scripts/scriptList.vue'),
      meta: {
        title: '常用脚本列表',
        icon: 'dashboard-line',
      },
    },
    {
      path: 'scripts/scriptEdit',
      name: 'ScriptEdit',
      component: () => import('@/views/sys/scripts/scriptEdit.vue'),
      meta: {
        title: '常用脚本编辑',
        icon: 'dashboard-line',
      },
    }
  ],
}
