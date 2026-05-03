package com.dstz.sys.api;

import java.util.Map;

/**
 * @Name SystemVariableApi
 * @description: 获取系统变量接口
 * @date 2023/8/1014:51
 */
public interface SystemVariableApi {

    /**
     * 获取所有的系统常量
     * key  key 如： currentUserId，value：获取变量的函数，执行了函数，获取真正的值
     *
     * 该map 是懒加载模式，不能遍历，不能判断是否包含，只能get获取其中的值
     * @return
     */
    Map<String,Object> getVariableMap();


    /**
     * 获取 指定字符串中 包含的系统常量
     * key  key 如： currentUserId，value：16452347899
     * 只支持 ${xxx} 形式的变量
     * @param str 指定的字符串
     * @return 字符串 中包含的系统常量
     */
    Map<String,Object> getVariableMap(String str);

    /**
     * 判断字符串中是否包含脚本写法
     *
     * @param value 要判断的字符串
     *
     * @return boolean 是否包含 true 包含 false 不包含
     */
    boolean containScript(String value);

    /**
     * <pre>
     * 返回变量真正的值
     * ps:如果是脚本的话，返回的可能是其他类型，所以是object
     *
     * </pre>
     *
     * @param val ${valName} #{valName}
     * @return
     */
    Object getVariableValue(Object val);
}
