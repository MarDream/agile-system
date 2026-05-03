package com.dstz.sys.api.dto;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Name SysDataSourceDefAttribute
 * @description: 数据源模板属性
 * @date 2022/3/716:41
 */
public class SysDataSourceDefAttribute implements Serializable {
    private static final long serialVersionUID = -2623523053919830352L;

    /**
     * 名字
     */
    @NotEmpty
    private String name;
    /**
     * 描述
     */
    @NotEmpty
    private String comment;
    /**
     * 参数类型
     */
    @NotEmpty
    private String type;
    /**
     * 是否必填
     */
    private boolean required;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 值，这个字段
     */
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
