 export  interface IdefaultModel {
	APP_NAME: string;
	//首页地址
	DASHBOARD_URL:string;
	//版本号
	APP_VER: string

	//内核版本号
	CORE_VER: string,

	//接口地址
	API_URL: string;

	//请求超时
	TIMEOUT: number,

	//TokenName
	TOKEN_NAME: string;

	//Token前缀，注意最后有个空格，如不需要需设置空字符串
	TOKEN_PREFIX: string;

	TOKEN_TIME:number, // token失效时间（分钟）
	//追加其他头
	HEADERS: object;

	//请求是否开启缓存
	REQUEST_CACHE: boolean;

	//布局 默认：default | 通栏：header | 经典：menu | 功能坞：dock
	//dock将关闭标签和面包屑栏
	LAYOUT:string;

	//菜单是否折叠
	MENU_IS_COLLAPSE:  boolean;

	//菜单是否启用手风琴效果
	MENU_UNIQUE_OPENED:  boolean;

	//是否开启多标签
	LAYOUT_TAGS:  boolean;

	//语言
	LANG: string;
	//主题颜色
	COLOR: string;

	//是否加密localStorage, 为空不加密，可填写AES(模式ECB,移位Pkcs7)加密
	LS_ENCRYPTION: string;

	//localStorageAES加密秘钥，位数建议填写8的倍数
	LS_ENCRYPTION_key:string

	//控制台首页默认布局
	DEFAULT_GRID:GridModel;
    THEME:string,
	MY_SHOW_LOGIN_OAUTH:boolean
}
export interface GridModel {
    layout:Array<number>;
    copmsList:Array<Array<string>>
}
