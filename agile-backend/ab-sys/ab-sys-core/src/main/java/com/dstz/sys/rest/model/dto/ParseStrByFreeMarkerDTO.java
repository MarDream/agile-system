package com.dstz.sys.rest.model.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Name ParseStrByFreeMarkerDTO
 * @description: 前端格式化字符串请求DTO
 * @date 2023/12/115:28
 */
public class ParseStrByFreeMarkerDTO implements Serializable {
    private static final long serialVersionUID = 6777364995760673690L;

    @NotNull(message = "字符串不能为空")
    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "ParseStrByFreeMarkerDTO{" +
                "str='" + str + '\'' +
                '}';
    }
}
