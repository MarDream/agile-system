package com.dstz.sys.api;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.utils.BeanCopierUtils;
import com.dstz.sys.api.dto.SysFileDTO;
import com.dstz.sys.core.entity.SysFile;
import com.dstz.sys.core.manager.SysFileManager;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @Name SysFileServiceImpl
 * @description: 文件附件服务
 * @date 2022/2/249:38
 */
@Service
public class SysFileApiImpl implements SysFileApi {
    private final SysFileManager sysFileManager;

    public SysFileApiImpl(SysFileManager sysFileManager) {
        this.sysFileManager = sysFileManager;
    }

    @Override
    public SysFileDTO upload(InputStream is, String fileName) {
        SysFile file = sysFileManager.upload(is, fileName, null);
        return BeanCopierUtils.transformBean(file, SysFileDTO.class);
    }

    @Override
    public void upload(InputStream is, String fileId, String fileName) {
        SysFile file = sysFileManager.getById(fileId);
        //如果文件不存在,则直接创建,存在则修改path和名称即可.
        if (ObjUtil.isNull(file)) {
            sysFileManager.upload(is, fileId, fileName, null);
        } else {
            String path = sysFileManager.uploader(is, fileId, fileName, null);
            sysFileManager.update(null, Wrappers.lambdaUpdate(SysFile.class).eq(SysFile::getId, fileId).set(SysFile::getName, fileName).set(SysFile::getPath, path));
        }
    }

    @Override
    public InputStream download(String fileId) {
        return sysFileManager.download(fileId);
    }

    @Override
    public void delete(String... fileId) {
        if (ArrayUtil.isEmpty(fileId)) {
            return;
        }
        for (String id : fileId) {
            sysFileManager.delete(id);
        }
    }
    
    @Override
    public SysFileDTO getById(String fileId) {
        return BeanCopierUtils.transformBean(sysFileManager.getById(fileId), SysFileDTO.class);
    }
}
