import { _GettersTree } from "pinia";
export interface IUserInfo {
    userName:string,
    userNameF:string,
}
export interface IUserInfoState {
    userInfo?:IUserInfo | null
    currentOrg: any
    buttonPermission: any
    abUser:any
}
export interface IUserInfoGetter extends _GettersTree<IUserInfoState> {
    GetUsers(user:IUserInfoState):IUserInfo | null;
}
export interface IUserInfoActions {
    // 当前用户JSON
    setUsers(user:IUserInfo):void;
    clearUsers():void;
    // 当前组织
    setCurrentOrg(org:any):void;
    setAbUsers(user:any):void;
    // 当前用户按钮操作权限
    setButtonPermission(btnPermission:any):void;
}