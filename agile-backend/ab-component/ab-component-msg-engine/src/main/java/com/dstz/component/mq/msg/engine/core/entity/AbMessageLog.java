package com.dstz.component.mq.msg.engine.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.dstz.base.entity.AbModel;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 第三方消息发送日志
 * </p>
 *
 * @since 2023-10-17
 */
@TableName("ab_message_log")
public class AbMessageLog extends AbModel<AbMessageLog> {

    @TableId(value = "id_", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * bpm关联业务id
     */
    @TableField("business_id_")
    private String businessId;

    /**
     * bpm关联业务类型
     */
    @TableField("business_type_")
    private String businessType;

    /**
     * bpm实例id
     */
    @TableField("inst_id_")
    private String instId;

    /**
     * 第三方关联id
     */
    @TableField("out_track_id_")
    private String outTrackId;

    /**
     * 消息类型 1钉钉 
     */
    @TableField("msg_type_")
    private String msgType;

    /**
     * 回调状态 0 未成功 1成功
     */
    @TableField("call_back_status_")
    private Integer callBackStatus;

    /**
     * 创建人ID
     */
    @TableField(value = "create_by_", fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 所属组织
     */
    @TableField(value = "create_org_id_", fill = FieldFill.INSERT)
    private String createOrgId;

    /**
     * 更新人
     */
    @TableField(value = "updater_", fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(value = "update_time_", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 更新人ID
     */
    @TableField(value = "update_by_", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(value = "create_time_", fill = FieldFill.INSERT)
    private Date createTime;
    
    @Override
    public String getId() {
        return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getInstId() {
        return instId;
    }
    
    public void setInstId(String instId) {
        this.instId = instId;
    }
    
    public String getOutTrackId() {
        return outTrackId;
    }
    
    public void setOutTrackId(String outTrackId) {
        this.outTrackId = outTrackId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Integer getCallBackStatus() {
        return callBackStatus;
    }
    
    public void setCallBackStatus(Integer callBackStatus) {
        this.callBackStatus = callBackStatus;
    }
    
    @Override
    public String getCreateBy() {
        return createBy;
    }
    
    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    
    public String getCreateOrgId() {
        return createOrgId;
    }
    
    public void setCreateOrgId(String createOrgId) {
        this.createOrgId = createOrgId;
    }
    
    @Override
    public String getUpdater() {
        return updater;
    }
    
    @Override
    public void setUpdater(String updater) {
        this.updater = updater;
    }
    
    @Override
    public Date getUpdateTime() {
        return updateTime;
    }
    
    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String getUpdateBy() {
        return updateBy;
    }
    
    @Override
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    
    @Override
    public Date getCreateTime() {
        return createTime;
    }
    
    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
