package com.dstz.component.mq.msg.engine.constants;

import com.dstz.base.common.codes.IBaseCode;

/**
 * msg实现相关响应码
 *
 */
public enum MsgEngineStatusCode implements IBaseCode {



    PARAM_TEMPLATE_CODE_MISS("param_template_code_miss","模板code参数缺失"),
    GET_DATA_BY_CODE_IS_NULL("get_data_by_code_is_null","根据code未查到消息模板数据code：{}"),
    TEMPLATE_PARAM_FORMAT_ERROR("template_param_format_error","模板参数格式化错误，请传入正确格式的模板参数"),
    NOT_FIND_LOG_REALIZE("not_find_log_realize","找不到类型[{}]的上传器的实现类"),
    MSG_LOG_ERROR("msg_log_error","消息日志记录失败{}"),
    UPDATE_MSG_LOG_STATUS_ERR("update_msg_log_status_err","更新消息日志记录失败{}"),
    MQ_SEND_FAIL("mq_send_fail", "站内信发送失败,缺少必要参数: {}"),

    SMS_SEND_FAIL("sms_send_fail", "短信发送失败: {}"),

    EMAIL_SEND_FAIL("email_send_fail", "邮件发送失败: {}"),

    MQ_SEND_ERROR("mq_send_error", "消息发送失败: {}"),

    ;


    private final String code;
    private final String desc;

    MsgEngineStatusCode(String code, String description) {
        this.code = code;
        this.desc = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return desc;
    }
}
