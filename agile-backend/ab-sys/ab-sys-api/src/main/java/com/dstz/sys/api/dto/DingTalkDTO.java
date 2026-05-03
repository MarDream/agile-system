package com.dstz.sys.api.dto;

public class DingTalkDTO {
    public String corpId;//企业唯一标识
    public Long   agentId;//应用唯一标识
    public String appKey;//公匙
    public String appSecret;//私钥
    public String tmpId;//卡片消息模板标识
    public String callBackSecret;//回调密钥
    public String pc_redirect_uri;//回调密钥

    public DingTalkDTO() {
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getTmpId() {
        return tmpId;
    }

    public void setTmpId(String tmpId) {
        this.tmpId = tmpId;
    }

    public String getCallBackSecret() {
        return callBackSecret;
    }

    public void setCallBackSecret(String callBackSecret) {
        this.callBackSecret = callBackSecret;
    }

    public String getPc_redirect_uri() {
        return pc_redirect_uri;
    }

    public void setPc_redirect_uri(String pc_redirect_uri) {
        this.pc_redirect_uri = pc_redirect_uri;
    }
}
