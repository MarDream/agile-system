package com.dstz.component.mq.engine.consumer;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.enums.ErrorLogLeve;
import com.dstz.base.common.events.AbErrorLogEvent;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.component.mq.api.JmsHandler;
import com.dstz.component.mq.api.model.JmsDTO;
import com.dstz.component.mq.engine.constants.MqEngineConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dstz.component.mq.engine.constants.MqExceptionCodeConstant.SEND_ERROR;

/**
 * 抽象公共消息队列
 *
 */
public abstract class AbstractMessageQueue implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageQueue.class);

    private ApplicationContext applicationContext;

    /**
     * 注册消息处理器
     */
    private Map<String, JmsHandler<Serializable>> registerJmsHandler = Collections.emptyMap();

    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null || MqEngineConstant.APPLICATION_CONTEXT_EVENT_ID.equals(event.getApplicationContext().getParent().getId())) {
            this.applicationContext = event.getApplicationContext();
            this.registerJmsHandler = applicationContext.getBeansOfType(JmsHandler.class).values().stream().collect(Collectors.toMap(JmsHandler::getType, o -> o));
            containerInitialCompleteAfter();
        }
    }

    /**
     * 容器初始化完成后
     */
    protected void containerInitialCompleteAfter() {
    }

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取消息处理实现
     *
     * @return 消息处理实现
     */
    protected JmsHandler<Serializable> getJmsHandler(String type) {
        return this.registerJmsHandler.get(type);
    }

	protected void invokeJmsHandler(JmsDTO<Serializable> jmsDTO) {
		JmsHandler<Serializable> jmsHandler = getJmsHandler(jmsDTO.getType());
		if (jmsHandler == null) {
			String formatMessage = StrUtil.format("未找到JmsHandler, 调用参数：{}", JsonUtils.toJSONString(jmsDTO));
			SpringUtil.publishEvent((AbErrorLogEvent.createErrorLog(new IllegalArgumentException(formatMessage), ErrorLogLeve.WARING)));
			return;
		}
		try {
			jmsHandler.handlerMessage(jmsDTO);
		} catch (Throwable e) {
			SpringUtil.publishEvent((AbErrorLogEvent.createErrorLog(new IllegalArgumentException(e), ErrorLogLeve.ERROR)));
		}
	}
}
