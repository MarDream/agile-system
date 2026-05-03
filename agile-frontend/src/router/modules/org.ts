import Layout from '@/layout/index.vue'
export default {
  path: '/org',
  name: 'ORG',
  component: Layout,
  meta: {
    title: '组织',
    icon: 'home-2-line',
    breadcrumbHidden: true,
  },
  children: [
    {
      path: '/org/user/userList',
      name: 'UserList',
      component: () => import('@/views/org/user/userList.vue'),
      meta: {
        title: '用户管理',
        icon: 'home-2-line',
      },
    },
    {
      path: '/org/user/userEdit',
      name: 'userEdit',
      component: () => import('@/views/org/user/userEdit.vue'),
      meta: {
        title: '编辑用户',
        icon: 'dashboard-line',
        noKeepAlive:true,
      },
    },
    {
      path: '/org/group/groupList',
      name: 'GroupList',
      component: () => import('@/views/org/group/groupList.vue'),
      meta: {
        title: '组织管理',

      },
    },
    {
      path: '/org/group/groupUserList',
      name: 'GroupUserList',
      component: () => import('@/views/org/group/groupUserList.vue'),
      meta: {
        title: '组织人员管理',
        icon: 'grid-fill',
      },
    },
    {
      path: '/org/role/list',
      name: 'RoleList',
      component: () => import('@/views/org/role/roleList.vue'),
      meta: {
        title: '角色管理',
        icon: 'grid-fill',
      },
    },
    {
      path: '/org/role/edit',
      name: 'RoleEdit',
      component: () => import('@/views/org/role/roleEdit.vue'),
      meta: {
        title: '角色编辑',
        icon: 'grid-fill',
        noKeepAlive:true,
      },
    },
    {
      path: '/org/role/users',
      name: 'RoleUserList',
      component: () => import('@/views/org/role/roleUserList.vue'),
      meta: {
        title: '角色用户管理',
        icon: 'grid-fill',
      },
    },
  ],
}
