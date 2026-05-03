package com.dstz.component.mq.engine.config;

import com.dstz.component.mq.engine.producer.AsyncJmsProducer;

/**
 * 消息队列类型
 *
 */
public enum AbMessageQueueType {

    /**
     * redis
     */
    REDIS(AbRedisMessageQueueConfiguration.class),

    /**
     * java消息队列
     */
    JMS(AbMessageQueueConfiguration.class),

    /**
     * 同步方式
     */
    SYNCHRONOUS(AbSynchronousMessageQueueConfiguration.class),


    /**
     * 丢弃消息
     */
    DISCARD(DiscardMessageQueueConfiguration.class),

    /**
     * 异步处理
     */
    ASYNC(AsyncJmsProducer.class);

    /**
     * 配置类
     */
    private Class<?> configurationClass;

    AbMessageQueueType(Class<?> configurationClass) {
        this.configurationClass = configurationClass;
    }

    public Class<?> getConfigurationClass() {
        return configurationClass;
    }
}
