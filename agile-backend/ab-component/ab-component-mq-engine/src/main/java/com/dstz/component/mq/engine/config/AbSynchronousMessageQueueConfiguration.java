package com.dstz.component.mq.engine.config;

import com.dstz.component.mq.api.producer.JmsProducer;
import com.dstz.component.mq.engine.producer.SynchronousQueueProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

/**
 * 同步消息队列配置
 *
 */
@Conditional(AbMessageQueueConditional.class)
public class AbSynchronousMessageQueueConfiguration {

    /**
     * 默认消息发送提供者
     *
     * @return 消息发送提供端
     */
    @Bean
    public JmsProducer jmsProducer() {
        return new SynchronousQueueProducer();
    }

}
