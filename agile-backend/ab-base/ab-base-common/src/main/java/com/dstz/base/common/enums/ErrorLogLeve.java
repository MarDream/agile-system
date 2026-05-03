package com.dstz.base.common.enums;

/**
 * @Name ErrorLogLeve
 * @description: 错误日志级别
 * @date 2023/8/3114:12
 */
public enum ErrorLogLeve {
    ERROR("error", "错误"),
    WARING("waring", "警告"),
    ;

    private String leve;
    private String desc;

    ErrorLogLeve(String leve, String desc) {
        this.leve = leve;
        this.desc = desc;
    }

    public String getLeve() {
        return leve;
    }

    public String getDesc() {
        return desc;
    }
}
