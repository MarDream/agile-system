package com.dstz.sys.api.dto;

import javax.validation.constraints.NotEmpty;

public class SysFileDownloadDto {
    /**
     * 文件id
     */
    @NotEmpty(message = "文件id不能为空")
    private String id;
    /**
     * 通过token加密文件id后的密匙
     */
    @NotEmpty(message = "下载密匙不能为空")
    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
