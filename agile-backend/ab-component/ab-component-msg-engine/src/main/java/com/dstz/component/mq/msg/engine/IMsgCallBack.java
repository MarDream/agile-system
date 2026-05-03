package com.dstz.component.mq.msg.engine;

import com.dstz.component.mq.api.model.JmsDTO;

import java.util.List;

public interface IMsgCallBack {
    /**
     *
     * 消息类型
     * @return
     */
    String type();

    /**
     *
     * @param outTrackId 第三方关联id
     */
    void msgCallBack(String outTrackId,String businessType);

}
