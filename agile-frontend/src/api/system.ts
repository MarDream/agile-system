import { IRouteMenu } from "@/models/IRouterMenu";
export const useMenuApi = (): Array<IRouteMenu> => {
  let serverData: Array<IRouteMenu> = [
    {
      name: "home",
      path: "/home",
      meta: {
        title: "首页",
        icon: "HomeFilled",
        type: "menu"
      },
      children: [
        {
          name: "dashboard",
          path: "/dashboard",
          meta: {
            title: "控制台",
            icon: 'Menu',
            affix: true,
            type: 'menu',
            parentPath: '/home'
          },
          component: "../views/home/index.vue"
        },
        {
          name: "news",
          path: "/news",
          meta: {
            title: "新闻",
            icon: 'Menu',
            type: 'menu',
            parentPath: '/home'
          },
          component: "../views/cms/news/newsList.vue"
        }
      ]
    },
    {
      name: "setting",
      path: "/setting",
      meta: {
        title: "配置",
        icon: "Setting",
        type: "menu"
      }
    },
    {
      name: "intertest",
      path: "/intertest",
      meta: {
        title: "内部测试",
        icon: "Setting",
        type: "menu"
      },
      children: [
        {
          path: "/intertest/test",
          name: "test",
          meta: {
            title: "我的测试",
            icon: 'Tools',
            type: "menu",
            parentPath: '/intertest'
          },
          component: "../views/testform/index.vue"
        },
        {
          path: "/intertest/test2",
          name: "test2",
          meta: {
            title: "我的测试2",
            icon: 'Tools',
            type: "menu",
            parentPath: '/intertest'
          },
          component: "../views/testform/test2.vue"
        }
      ]
    }
  ];
  return serverData
}