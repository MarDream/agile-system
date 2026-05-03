package com.dstz.component.mq.msg.engine.core.manager;

import com.dstz.base.manager.AbBaseManager;
import com.dstz.component.mq.msg.engine.core.entity.AbMessageLog;

import java.util.List;

/**
 * <p>
 * 第三方消息发送日志 通用业务类
 * </p>
 *
 * @since 2023-10-17
 */
public interface AbMessageLogManager extends AbBaseManager<AbMessageLog> {

    /**
     * 根据任务id更新回调状态
     * @param businessId 业务id
     * @return 受影响行数
     */
    Integer updateMsgLogStatusByBusinessId(String businessId,String type);

    /**
     * 根据任务id获取未处理的消息日志
     * @param businessId 业务id
     * @return
     */
    List<AbMessageLog> getByBusinessId(String businessId);

}
