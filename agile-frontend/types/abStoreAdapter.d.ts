declare interface AbStoreAdapter {
  buttonPermission: any
  userMenuList: any
  getAbUser: AbUser
  currentOrg: any
}

declare interface AbUser {
  fullName: string
  userId: string
  username: string
}

declare interface TabEditInfo {
  fullPath: string 
 // name?: string
  meta: TabMeta
}

declare interface TabMeta {
  // 要修改的标题
  title: string
 /*  badge?: string
  // 是否显示小圆点
  dot?: boolean
  // 图标
  icon?: string
  // 当前路由是否可关闭多标签页
  noClosable?: boolean */
}
