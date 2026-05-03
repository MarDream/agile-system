export interface IRouteMenu {
    name:string;
    path:string;
    component?:string;
    children?:Array<IRouteMenu>,
    redirect?:string,
    meta:IRouteMate,
  
}
export interface IRouteMate{
    topMenuCode: any;
    title:string;
    icon:string;
    type:string;
    affix?:boolean,
    parentPath?:string,
    hidden?:boolean
}

// name: "intertest",
// path: "/intertest",
// meta: {
//   title: "内部测试",
//   icon: "Setting",
//   type: "menu"
// },
// children: [
//   {
//     path: "/intertest/test",
//     name: "test",
//     meta: {
//       title: "我的测试",
//       icon: 'Tools',
//       type: "menu"
//     },
//     component: "../views/testform/index.vue"
//   },