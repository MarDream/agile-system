import Layout from '@/layout/index.vue'
export default {
  path: '/cms',
  name: 'CMS',
  component: Layout,
  meta: {
    title: 'CMS',
    icon: 'home-2-line',
    breadcrumbHidden: true,
  },
  children: [
    {
      path: '/cms/notice/noticeList',
      name: 'NoticeList',
      component: () => import('@/views/cms/notice/noticeList.vue'),
      meta: {
        title: '公告列表',
        icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/notice/noticeEdit',
      name: 'NoticeEdit',
      component: () => import('@/views/cms/notice/noticeEdit.vue'),
      meta: {
        title: '编辑公告列表',
        icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/notice/noticeDetails',
      name: 'NoticeDetails',
      component: () => import('@/views/cms/notice/noticeDetails.vue'),
      meta: {
        title: '公告列表详情',
         icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/notice/noticeIndexList',
      name: 'NoticeIndexList',
      component: () => import('@/views/cms/notice/noticeIndexList.vue'),
      meta: {
        title: '公告详情列表预览',
         icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/news/newsList',
      name: 'NewsList',
      component: () => import('@/views/cms/news/newsList.vue'),
      meta: {
        title: '新闻列表',
         icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/news/newsIndexList',
      name: 'NewsIndexList',
      component: () => import('@/views/cms/news/newsIndexList.vue'),
      meta: {
        title: '新闻详情列表预览',
         icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/news/newsEdit',
      name: 'NewsEdit',
      component: () => import('@/views/cms/news/newsEdit.vue'),
      meta: {
        title: '新闻编辑',
         icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    },
    {
      path: '/cms/news/newsDetails',
      name: 'NewsDetails',
      component: () => import('@/views/cms/news/newsDetails.vue'),
      meta: {
        title: '新闻详情',
         icon: 'Menu',
        affix: true,
        type: 'menu',
        parentPath: '/cms'
      },
    }
  ],
}
