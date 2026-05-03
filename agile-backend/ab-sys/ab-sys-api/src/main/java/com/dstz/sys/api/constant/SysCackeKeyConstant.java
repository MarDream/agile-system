package com.dstz.sys.api.constant;
/**
 * 系统缓存key定义
 * @since 2022-08-17
 */
public class SysCackeKeyConstant {


    /**
     * 字典方法缓存key
     */
    public static final String GET_DICT_NODE_LIST = "getDictNodeListEl:";

    /**
     * 字典方法缓存key SpEL表达式
     */
    public static final String GET_DICT_NODE_LIST_EL = "'getDictNodeListEl:'";

    /**
     * 字典方法缓存key SpEL表达式
     */
    public static final String GET_DICT_NODE_LIST_RECEIVE_EL = GET_DICT_NODE_LIST_EL + ".concat(#root.args[0])";


    /**
     * 消息模板方法缓存key SpEL表达式
     */
    public static final String GET_MESSAGE_TEMPLATE_LIST = "'getMessageTemplateListEl:'";

    /**
     * 字典方法缓存key SpEL表达式
     */
    public static final String ET_MESSAGE_TEMPLATE_LIST_RECEIVE_EL = GET_MESSAGE_TEMPLATE_LIST + ".concat(#root.args[0])";

    /**
     * 系统配置缓存key
     */
    public static final String PROPERTIES_CACHE_REGION_RECEIVE_KEY = "getPropertiesCacheEl:" ;


    public static final String PROPERTIES_CACHE_REGION_RECEIVE_KEY_EL = "'getPropertiesCacheEl:'" ;

    /**
     * 系统配置缓存key SpEL表达式
     */
    public static final String PROPERTIES_CACHE_REGION_RECEIVE_EL = PROPERTIES_CACHE_REGION_RECEIVE_KEY_EL + ".concat(#root.args[0])";

    public static final String INF_WHITELIST_KEY = "infWhiteList";

    public static final String INF_INGORES_KEY = "infIngores";

    public static final String INF_CSRF_INGORES_KEY = "infCsrfIngores";


    /**
     * 缓存区域-登录用户公司
     */
    public static final String REGION_LOGIN_COMPANY = "LOGIN_COMPANY";


    /**
     * 用户工作接收-EL表达式
     *
     * <p>userWorkReceive:{userId}</p>
     */
    public static final String USER_WORK_RECEIVE_EL = "'userWorkReceive:'.concat(#root.args[0])";

    /**
     * 用户工作接收-字符串格式化
     *
     * <p>userWorkReceive:{userId}</p>
     */
    public static final String USER_WORK_RECEIVE_FORMAT = "userWorkReceive:%s";
}
