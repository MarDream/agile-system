package com.dstz.base.common.property;

/**
 * @Name PropertyEnum
 * @description: 属性管理枚举
 * @date 2022/3/915:12
 */
public enum PropertyEnum implements IBaseProperty {


    ADMIN_ACCOUNTS("admin.accounts", "admin", "admin账户"),
    JDBC_TYPE("spring.jdbc.dbType", "", "数据库类型"),
    FORM_DEF_BACKUP_PATH("formDefBackupPath", "", "表单备份地址"),
    FORM_TEMPLATE_URL("formTemplateUrl", "src/main/resources/templates", "表单模板URL地址"),

    // 流程相关

    TASK_REMOVE_MESSAGE_PUSH("task_remove_message_push", false, "任务删除消息推送"),
    DO_TASK_SUCCESSIONAL("doTaskSuccessional", false, "任务后面节点还是自己任务时，连续处理"),
    BPM_NOT_DEFAULT_BUTTONS("bpmNotDefaultButtons", "oppose,reject2Start,custMultiExecution,addSign,manualEnd", "流程非默认按钮"),

    // 上传相关
    UPLOADER_DEFAULT("uploader.default", "", "默认的文件上传器"),
    MINIO_BUCKET_NAME("ab.minio.bucketName", "", "minio bucketName"),
    UPLOADER_ORDINARY_PATH("uploader.ordinary.path", "", ""),
    BATCH_INSERT_EXCL_DATA_NUM("batch_insert_excl_data_num", 80, "导入excl数据时单次批量插入行数"),
    EXPORT_EXCL_MAX_NUM("export_excl_max_num", 50000, "导入excl最大行数"),
    // 用户登录
    LOGIN_COUNT("loginCount", 5, "连续登录验证失败最大次数"),
    LOGIN_FILED_LOCK_TIME_DESC("loginFailedlockTimeDesc", "24小时", "登录失败锁定账户的时间描述,请自行和缓存时间配置匹配，缓存配置24h，这里建议配置24小时，默认为24小时"),
    PWD_LOSE_COUNT("pwdLose", 180, "修改密码下次到期时间"),
    PWD_CHECK_RULE_KEY("checkingRuleKey", "^[A-Za-z0-9_!@#$%&*]{6,20}$", "密码规则"),
    PWD_CHECK_RULE_TXT("checkingRuleTxt", "密码长度在6-20位之间由数字、字母组合", "密码规则提示"),
    IS_OPEN_RESET_PWD_BY_EMAIL("isOpenResetPwdByEmail", false, "是否开启邮件找回密码功能"),
    LOGIN_CAPTCHA_KEY("captchaSwitch", false, "用户登陆是否校验验证码"),
    LOGIN_RESET_PWD("isResetPwd", false, "初始化密码是否必须重置后登录"),
    CHANGE_PWD_iS_LOG_OUT("changePwdIsLogOut", false, "修改密码是否退出登录"),
    CHANGE_PWD_iS_Exit_SYSTEM("changePwdIsExitSystem", false, "修改密码是否强制退出系统"),

    // 企业微信
    WX_QY_APP_SECRET("wxqy_appsecret", "", ""),

    //邮箱配置
    EMAIL_HOST("email_host", "", "email_host"),
    EMAIL_PORT("email_port", "", "email_port"),
    EMAIL_SSL("email_ssl", "", "email_ssl"),
    EMAIL_NICKNAME("email_nickname", "", "email_nickname"),
    EMAIL_ADDRESS("email_address", "", "email_address"),
    EMAIL_PASSWORD("email_password", "", "email_password"),


    IS_INTERFACE_AUTH("is_interface_auth", false, "是否开启接口级别鉴权"),

    /**
     * 事务消息重试次数
     */
    TRXM_RETRY_TIMES("ab.trxm.retry-times", 3, "事务消息重试次数"),

    /**
     * 百度SDK APPID
     */
    BAIDU_SDK_APPID("baidu.sdk.app-id", "", "百度SDK APPID"),

    /**
     * 百度SDK 密钥
     */
    BAIDU_SDK_SECRET("baidu.sdk.secret", "", "百度SDK 密钥"),


    ONLINE_DOC_SERVICE_URL("online_doc_service_url","http://192.168.1.141:18080","在线文档服务地址"),
    AGILEBPM_SERVICE_URL("agilebpm_service_url","http://192.168.1.6:8080","平台服务地址"),
    ONLINE_DOC_SERVICE_AGENT_PREFIX("online_doc_service_agent_prefix","/weboffice","在线文档服务代理前缀"),

    PC_URL("pcUrl","http://localhost:8086","pc端服务地址"),
    APP_URL("appUrl","http://localhost:5173/#","移动端服务端的地址"),
    MOBILE_URL("mobileUrl","http://localhost:5173","移动端服务端的地址"),
    WORD_OFFICE_HOME("ab.word.office-home", "", "LibreOfficePortable文件位置"),
    WORD_OFFICE_HOST("ab.word.office-host", "", "LibreOfficePortable线上版"),
    SCRIPT_LOG("script_log", "false", "脚本日志开关"),

    DISABLED_AREA("DISABLED_AREA","河南省.郑州市,福建省.莆田市,","产品禁用地"),

    /**
     * token是否放入cookie
     */
     tokenCookieSwitch("ab.security.token-cookie-switch", false, "token是否放入cookie"),

    /**
     * 系统临时用户名
     */
    SYS_TEMP_USER_NAME("sys.temporary.user.name","temporaryUsers","系统临时用户名"),


    /**
     * 系统临时开放应用id
     */
    SYS_TEMP_OPEN_APP_ID("sys.temporary.open.appId","openPlatform","系统临时开放应用id"),

    /**
     * 前端加密密钥
     */
    LE_ENCRYPTION_KEY("ab.secret-key", "am2.8basic", "前端加密密钥"),

    /**
     * 后端接口Nginx代理名称
     */
    API_NGINX_AGENT_PREFIX("api.nginx.agent.prefix","","后端接口Nginx代理名称"),
    
    /**
     * 编码是否默认增加scode
     */
    IS_SCODE("isScode", true, "编码是否默认增加scode"),

    SALARY_DEF_KEY("salary_def_key", "gzffqr", "薪酬管理默认启动流程key"),

    SALARY_BO_KEY("salary_bo_key", "gzt", "薪酬管理默认启动流程key"),




    USER_REGISTER_BPM_DEF_KEY("userRegisterBpmDefKey","dstz_appstore_user_register","用户注册流程定义key"),
    USER_REGISTER_BO_CODE("userRegisterBoCode","dstz_appstore_user","用户注册业务对象code"),

    DD_CALL_BACK_URL("dd_call_back_url","/api/ab-bpm/bpm/task/dealTaskByThird","钉钉卡片点击同意时的回调注册地址"),
    ;


    private final String key;

    private final Object defaultValue;

    private final String desc;

    PropertyEnum(String key, Object defaultValue, String desc) {
        this.key = key;
        this.defaultValue = defaultValue;
        this.desc = desc;
    }

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getDesc() {
		return this.desc;
	}

	@Override
	public Object getDefaultValue() {
		return this.defaultValue;
	}

}
