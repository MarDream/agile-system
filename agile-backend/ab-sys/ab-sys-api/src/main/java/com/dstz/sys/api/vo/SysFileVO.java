package com.dstz.sys.api.vo;

import java.io.Serializable;

/**
 * @Name SysFileInfoDTO
 * @description: 附件上传文件信息解析
 * @date 2024/3/1111:22
 */
public class SysFileVO implements Serializable {
    private static final long serialVersionUID = -4367971857124877587L;

    protected String id;
    /**
     * 附件名
     */
    private String name;

    /**
     * 附件大小
     */
    private Long size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SysFileVO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
