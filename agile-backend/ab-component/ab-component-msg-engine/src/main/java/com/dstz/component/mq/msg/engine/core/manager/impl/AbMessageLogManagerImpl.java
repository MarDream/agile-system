package com.dstz.component.mq.msg.engine.core.manager.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dstz.base.common.constats.NumberPool;
import com.dstz.base.manager.impl.AbBaseManagerImpl;
import com.dstz.component.mq.msg.engine.core.entity.AbMessageLog;
import com.dstz.component.mq.msg.engine.core.manager.AbMessageLogManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 第三方消息发送日志 通用服务实现类
 *
 * @since 2023-10-17
 */
@Service("abMessageLogManager")
public class AbMessageLogManagerImpl extends AbBaseManagerImpl<AbMessageLog> implements AbMessageLogManager {


    @Override
    public Integer updateMsgLogStatusByBusinessId(String businessId,String type) {
        return this.update(null,Wrappers.lambdaUpdate(AbMessageLog.class).set(AbMessageLog::getCallBackStatus, NumberPool.INTEGER_ONE).likeRight(AbMessageLog::getBusinessId,businessId).eq(AbMessageLog::getMsgType,type));
    }

    @Override
    public List<AbMessageLog> getByBusinessId(String businessId) {
        return this.selectByWrapper(Wrappers.lambdaQuery(AbMessageLog.class).likeRight(AbMessageLog::getBusinessId,businessId).eq(AbMessageLog::getCallBackStatus,NumberPool.INTEGER_ZERO));
    }
}
