export const  useMenu =() =>{
    let serverData = [
        {
          name: "home",
          path: "/home",
          meta: {
            title: "首页",
            icon: "el-icon-eleme-filled",
            type: "menu"
          },
          children: [
            {
              name: "dashboard",
              path: "/dashboard",
              meta: {
                title: "控制台",
                icon: "el-icon-menu",
                affix: true
              },
              component: "home"
            }
          ]
        },
        {
          name: "setting",
          path: "/setting",
          meta: {
            title: "配置",
            icon: "el-icon-setting",
            type: "menu"
          },
          children: [
            {
              path: "/setting/system",
              name: "system",
              meta: {
                title: "系统设置",
                icon: "el-icon-tools",
                type: "menu"
              },
              component: "setting/system"
            },
            {
              path: "/setting/user",
              name: "user",
              meta: {
                title: "用户管理",
                icon: "el-icon-user-filled",
                type: "menu"
              },
              component: "setting/user"
            }
          ]
        }
      ];
     return serverData
}