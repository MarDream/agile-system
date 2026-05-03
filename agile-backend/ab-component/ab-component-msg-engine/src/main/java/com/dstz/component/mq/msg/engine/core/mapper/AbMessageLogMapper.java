package com.dstz.component.mq.msg.engine.core.mapper;

import com.dstz.base.mapper.AbBaseMapper;
import com.dstz.component.mq.msg.engine.core.entity.AbMessageLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 第三方消息发送日志 Mapper 接口
 * </p>
 *
 * @since 2023-10-17
 */
@Mapper
public interface AbMessageLogMapper extends AbBaseMapper<AbMessageLog> {

}
