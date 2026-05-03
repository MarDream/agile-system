package com.dstz.base.common.property;

public interface SysConfigurationApi {
    /**
     * 根据code获取配置对象
     * @param code 配置code
     * @param typeClass 需要返回的类型
     * @return
     */
    <T> T getConfigByCode(String code, Class<T> typeClass);

    String getConfByCode(String code);

    /**
     * 保存配置
     *
     * @param json 配置json
     */
    void saveConf(String json);

    /**
     * 根据编码删除配置
     *
     * @param code 配置编码
     */
    void deleteByCode(String code);
}
