import Layout from '@/layout/index.vue'
import home from '@/views/home/index.vue'

export default {
  path: '/bpm',
  name: 'BPM',
  component: Layout,
  meta: {
    title: '流程管理',
    icon: 'home-2-line',
    breadcrumbHidden: true,
  },
  children: [
    {
      path: '/bpm/bpm/definitionList',
      name: 'BpmDefinitionList',
      component: () => import('@/views/bpm/definition/bpmDefinitionList.vue'),
      meta: {
        title: '流程列表',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/bpm/definitionEdit',
      name: 'BpmDefinitionEdit',
      component: () => import('@/views/bpm/definition/bpmDefinitionEdit.vue'),
      meta: {
        title: '流程编辑',
      },
    },
    {
      path: '/bpm/bpm/instanceList',
      name: 'BpmInstanceList',
      component: () => import('@/views/bpm/instance/bpmInstanceList.vue'),
      meta: {
        title: '实例列表',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/bpm/instanceDetail',
      name: 'BpmInstanceDetail',
      component: () => import('@/views/bpm/instance/bpmInstanceDetail.vue'),
      meta: {
        title: '流程实例详情',
        dynamicNewTab: true,
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/flowStart/:defKey',
      name: 'FlowStart',
      component: () => import('@/views/bpm/instance/flowStart.vue'),
      meta: {
        title: '流程启动',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/bpm/draftStart',
      name: 'DraftStart',
      component: () => import('@/views/bpm/instance/draftStart.vue'),
      meta: {
        title: '草稿启动',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/bpm/instanceApprovalHistory',
      name: 'BpmInstanceApprovalHistory',
      component: () =>
        import('@/views/bpm/instance/bpmInstanceApprovalHistory.vue'),
      meta: {
        title: '流程实例详情',
        dynamicNewTab: true,
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/bpm/taskList',
      name: 'BpmTaskList',
      component: () => import('@/views/bpm/task/bpmTaskList.vue'),
      meta: {
        title: '流程任务列表',
        icon: 'Menu',
      },
    },
    {
      path: '/bpm/bpm/taskComplate',
      name: 'BpmTaskComplate',
      component: () => import('@/views/bpm/task/bpmTaskComplate.vue'),
      meta: {
        title: '流程任务处理',
        icon: 'Menu',
        noKeepAlive: true,
      },
    } ,
    {
      path: '/bpm/my/todoList',
      name: 'BpmMyTaskTodoList',
      component: () => import('@/views/bpm/myTask/todoList.vue'),
      meta: {
        title: '我的待办列表',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/my/approveList',
      name: 'BpmMyApproveList',
      component: () => import('@/views/bpm/myTask/approveList.vue'),
      meta: {
        title: '我的审批历史',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/my/definitionList',
      name: 'BpmMyDefinitionList',
      component: () => import('@/views/bpm/myTask/definitionList.vue'),
      meta: {
        title: '可申请流程列表',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/my/applyList',
      name: 'BpmMyApplyList',
      component: () => import('@/views/bpm/myTask/applyList.vue'),
      meta: {
        title: '申请历史',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/my/draftList',
      name: 'BpmMyDraftList',
      component: () => import('@/views/bpm/myTask/draftList.vue'),
      meta: {
        title: '草稿',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/my/receiveList',
      name: 'BpmMyReceiveList',
      component: () => import('@/views/bpm/myTask/receiveList.vue'),
      meta: {
        title: '抄送/传阅',
        icon: 'Menu',
        noKeepAlive: false,
      },
    },
    {
      path: '/bpm/bpm/definitionVersionList',
      name: 'BpmDefinitionVersionList',
      component: () =>
        import('@/views/bpm/definition/overView/bpmDefinitionVersionList.vue'),
      meta: {
        title: '流程版本列表',
        icon: 'Menu',
        noKeepAlive: true,
      },
    },
    {
      name: "dashboard",
      path: "/dashboard",
      meta: {
        title: "控制台",
        icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/bpm'
      },
      component: home
    },
    {
      path: 'bpm/bpmInstanceListDetail',
      name: 'BpmInstanceListDetail',
      component: () => import('@/views/bpm/instance/bpmInstanceListDetail.vue'),
      meta: {
        title: '流程实例详情',
        dynamicNewTab: true,
        icon: 'grid-fill',
        noKeepAlive: false,
      },
    },
  ],
}
