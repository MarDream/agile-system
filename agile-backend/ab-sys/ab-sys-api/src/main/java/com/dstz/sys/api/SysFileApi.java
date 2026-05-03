package com.dstz.sys.api;

import com.dstz.sys.api.dto.SysFileDTO;

import java.io.InputStream;

/**
 * @Name SysFileApi
 * @description:文件附件服务
 * @date 2022/2/249:35
 */
public interface SysFileApi {

    /**
     * <pre>
     * 上传附件
     * </pre>
     *
     * @param is
     * @param fileName
     * @return
     */
    SysFileDTO upload(InputStream is, String fileName);

    /**
     * <pre>
     * 上传附件或修改附件(附件ID存在则修改, 不存在则上传)
     * </pre>
     *
     * @param is       文件流
     * @param fileId   文件ID
     * @param fileName 文件名称
     * @return
     */
    void upload(InputStream is, String fileId, String fileName);

    /**
     * <pre>
     * 下载附件
     * 返回流
     * </pre>
     *
     * @param fileId
     * @return
     */
    InputStream download(String fileId);

    /**
     * <pre>
     * 删除附件
     * 包括流信息
     * </pre>
     *
     * @param fileId
     */
    void delete(String... fileId);

	SysFileDTO getById(String fileId);

}
