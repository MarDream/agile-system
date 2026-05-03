package com.dstz.sys.core.manager.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.sys.core.entity.SysLogErr;
import com.dstz.sys.core.manager.SysLogErrManager;
import org.springframework.stereotype.Service;

/**
 * 系统异常日志 通用服务实现类
 *
 * @since 2022-02-17
 */
@Service("sysLogErrManager")
public class SysLogErrManagerImpl extends AbBaseManagerImpl<SysLogErr> implements SysLogErrManager {

    /**
     * 异常日志只允许更改状态,其他字段只能回显,禁止修改
     * @param entity 实体
     * @return
     */
    @Override
    public int update(SysLogErr entity) {
        return update(null, Wrappers.lambdaUpdate(SysLogErr.class)
                .eq(SysLogErr::getId, entity.getId())
                .set(SysLogErr::getStatus, entity.getStatus())
                .set(SysLogErr::getIpAddress,entity.getIpAddress()));
    }

}
