package com.dstz.base.event;

import com.dstz.base.model.AbDataSourceModel;
import org.springframework.context.ApplicationEvent;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * 数据源切换前
 *
 */
public class AbDataSourceEvent extends ApplicationEvent {

    private static final long serialVersionUID = 8850465955737343686L;

    /**
     * 事件类型
     */
    public enum EventType {
        /**
         * 数据源切换前
         */
        SWITCH_BEFORE,

        /**
         * 数据源移除后
         */
        REMOVE_AFTER,

        /**
         * 获取事务前
         */
        GET_BEFORE,
    }

    private final EventType eventType;

    public AbDataSourceEvent(AbDataSourceModel abDataSourceModel, EventType eventType) {
        super(abDataSourceModel);
        this.eventType = Objects.requireNonNull(eventType);
    }

    public AbDataSourceModel getAbDataSourceModel(){
        return (AbDataSourceModel) source;
    }

    public EventType getEventType() {
        return eventType;
    }

    /**
     * 如果是指定事件类型执行函数
     *
     * @param eventType 事件类型
     * @param consumer  函数
     */
    public void ifEventType(EventType eventType, Consumer<AbDataSourceModel> consumer) {
        if (this.eventType.equals(eventType)) {
            consumer.accept((AbDataSourceModel) getSource());
        }
    }


}
