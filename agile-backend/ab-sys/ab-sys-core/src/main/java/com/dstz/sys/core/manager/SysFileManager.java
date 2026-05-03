package com.dstz.sys.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.sys.core.entity.SysFile;
import com.dstz.sys.rest.model.dto.UpdateFileDTO;

import java.io.InputStream;

/**
 * <p>
 * 系统附件 通用业务类
 * </p>
 *
 * @since 2022-02-17
 */
public interface SysFileManager extends AbBaseManager<SysFile> {
    /**
     * <pre>
     * 上传附件
     * </pre>
     *
     * @param is
     * @param fileName
     * @param type
     * @return
     */
    SysFile upload(InputStream is, String fileName, String type);

    /**
     * <pre>
     * 上传指定ID的附件
     * </pre>
     *
     * @param is
     * @param fileId
     * @param fileName
     * @param type
     * @return
     */
    SysFile upload(InputStream is, String fileId, String fileName, String type);

    /**
     * <pre>
     * 只在uploader上传附件,不创建附件信息的数据
     * </pre>
     *
     * @param is
     * @param fileId
     * @param fileName
     * @param type
     * @return
     */
    String uploader(InputStream is, String fileId, String fileName, String type);

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
    void delete(String fileId);

    /**
     * <pre>
     * 更新附件
     * 更新只是更新数据 sys_file 数据中的path id 不会更改，
     * 会一直新增 db_upload 表中的数据
     * </pre>
     *
     * @param updateDTO
     * @return
     */
    int update(UpdateFileDTO updateDTO);
}
