package com.dstz.component.mq.msg.engine.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.dstz.base.common.constats.StrPool;
import com.dstz.base.common.exceptions.BusinessException;
import com.dstz.base.common.property.SysConfigurationApi;
import com.dstz.base.common.utils.AESUtil;
import com.dstz.base.common.utils.JsonUtils;
import com.dstz.component.mq.msg.engine.dto.MsgImplDTO;
import com.dstz.component.msg.api.dto.ExternalMsgDTO;
import com.dstz.component.msg.handler.AbsNotifyMessageHandler;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.dto.AliYunConfigDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.dstz.component.mq.api.constants.JmsTypeEnum.SMS;
import static com.dstz.component.mq.msg.engine.constants.MsgEngineStatusCode.SMS_SEND_FAIL;
import static com.dstz.component.mq.msg.engine.constants.MsgEnginesConstant.SMS_RECEIVERS;

/**
 * 短消息发送处理器
 *
 */
@Component("smsHandler")
public class SmsHandler extends AbsNotifyMessageHandler<MsgImplDTO> {

    @Override
    public String getType() {
        return SMS.getType();
    }

    @Override
    public boolean sendMessage(MsgImplDTO message) {

        if (CollUtil.isNotEmpty(message.getReceivers())) {
            for (IUser reciver : identityConvert.convert2Users(message.getReceivers())) {
                sendSms(reciver.getAttrValue("mobile", String.class), message, reciver.getAttrValue("fullName", String.class));
            }
        } else {
            if (MapUtil.isNotEmpty(message.getExtendVars()) && message.getExtendVars().containsKey(SMS_RECEIVERS)) {
                sendSms(String.join(",", (List) message.getExtendVars().get(SMS_RECEIVERS)), message, message.getExtendVars().containsKey("fullName") ? message.getExtendVars().get("fullName").toString() : "");
            }
        }

        return true;
    }

    /**
     * @param phoneNum
     * @param message
     * @param fullName 外部消息如果扩展参数中有该值短信会带上用户名
     */
    private void sendSms(String phoneNum, MsgImplDTO message, String fullName) {
        AliYunConfigDTO aliYunConfigDTO = SpringUtil.getBean(SysConfigurationApi.class).getConfigByCode("aliyunConfig", AliYunConfigDTO.class);
        if(ObjectUtil.isNull(aliYunConfigDTO)){
            LOGGER.warn("Not configured system configuration  aliyunConfig");
            throw new BusinessException(SMS_SEND_FAIL.formatDefaultMessage("未查询到有效短信配置"));
        }
        IAcsClient client = getIacsClient(aliYunConfigDTO);
        if (ObjectUtil.isNull(client) || StrUtil.isEmpty(phoneNum)) {
            LOGGER.warn("IAcsClient is null or user mobile is empty");
            throw new BusinessException(SMS_SEND_FAIL.formatDefaultMessage("SMS_CLIENT初始化失败，请检查短信配置"));
        }


        CommonRequest smsRequest = getCommonRequest(aliYunConfigDTO,message.getExtendVars());

        smsRequest.putQueryParameter("PhoneNumbers", phoneNum);

        setTemplateParam(smsRequest, fullName,message,aliYunConfigDTO);


        try {
            CommonResponse response = client.getCommonResponse(smsRequest);
            JsonNode jsonNode = JsonUtils.toJSONNode(response.getData());


            if (!"OK".equals(JsonUtils.getValueAsString("Code",jsonNode))) {
                LOGGER.error("发送短信失败:" + response.getData());
                iAcsClient = null;
                throw new BusinessException(SMS_SEND_FAIL.formatDefaultMessage(response.getData()));
            }
        } catch (Exception e) {
            iAcsClient = null;
            LOGGER.error("发送短信失败" + e.getMessage(), e);
            throw new BusinessException(SMS_SEND_FAIL.formatDefaultMessage(e.getMessage()));
        }
    }


    /**
     * 支持短信中自定义参数与模板。
     * 否则使用默认模板 【 ${name}您好，您有新的任务${subject}，请及时查阅。】 {$subject} 即为消息体内容
     *
     * @param smsRequest
     * @param extendVars
     */
    private void setTemplateParam(CommonRequest smsRequest, String FullName, MsgImplDTO msgImplDTO,AliYunConfigDTO aliYunConfigDTO) {


        String type = "审批";
        if (ObjectUtil.isNotEmpty(msgImplDTO.getCardTemplateData())&&msgImplDTO.getCardTemplateData().getExtendParam().containsKey("taskType") && "carbon".equals(msgImplDTO.getCardTemplateData().getExtendParam().get("taskType")) && ObjectUtil.isNotEmpty(msgImplDTO.getCardTemplateData().getExtendParam().get("carbonCopyRecordId"))) {
            type = "审阅";
        }
        if (ObjectUtil.isNotEmpty(msgImplDTO.getCardTemplateData())&&msgImplDTO.getCardTemplateData().getExtendParam().containsKey("taskType") && "reminder".equals(msgImplDTO.getCardTemplateData().getExtendParam().get("taskType"))) {
            type = "催办";
        }
        if (ObjectUtil.isNotEmpty(msgImplDTO.getCardTemplateData())&&msgImplDTO.getCardTemplateData().getExtendParam().containsKey("bpmTaskType") && "signTask".equals(msgImplDTO.getCardTemplateData().getExtendParam().get("bpmTaskType"))) {
            type = "会签";
        }

        smsRequest.putQueryParameter("TemplateCode", msgImplDTO.getExtendVars().containsKey("TemplateCode")? msgImplDTO.getExtendVars().get("TemplateCode").toString() : aliYunConfigDTO.getTemplateCode());
        msgImplDTO.getExtendVars().put("name", StrUtil.isNotEmpty(FullName)?FullName:StrPool.EMPTY);
        msgImplDTO.getExtendVars().put("type", type);
        msgImplDTO.getExtendVars().put("subject",msgImplDTO.getExtendVars().containsKey("title")?msgImplDTO.getExtendVars().get("title"):"");

        if(msgImplDTO.getExtendVars().containsKey(ExternalMsgDTO.SMSTYPE)){
            if(ObjectUtil.isNotEmpty(msgImplDTO.getExtendVars().get(ExternalMsgDTO.SMSTYPE)) && StrPool.TRUE.equals(msgImplDTO.getExtendVars().get(ExternalMsgDTO.SMSTYPE).toString()) &&msgImplDTO.getExtendVars().containsKey("code")){
                Map<String,Object> map = new HashMap<>();
                map.put("code",msgImplDTO.getExtendVars().get("code"));
                smsRequest.putQueryParameter("TemplateParam", JsonUtils.toJSONString(map));
            }
        }else{
            smsRequest.putQueryParameter("TemplateParam", JsonUtils.toJSONString(msgImplDTO.getExtendVars()));
        }
    }

    public static Map<String, String> filterMapValuesWithRegex(Map<String, Object> map) {
        // 正则表达式，匹配非0-9、a-z、A-Z的字符
        Pattern pattern = Pattern.compile("[^0-9a-zA-Z]");
        // 创建一个新的HashMap来存储过滤后的值
        Map<String, String> filteredMap = new HashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            // 如果值不为空
            if (value != null) {
                // 替换所有匹配正则表达式的字符为空字符串
                String filteredValue = pattern.matcher(value).replaceAll("");
                // 将过滤后的键值对添加到filteredMap中
                filteredMap.put(key, filteredValue);
            }
        }
        return filteredMap;
    }

    /**
     * 默认的请求
     *
     * @return
     */
    private CommonRequest getCommonRequest(AliYunConfigDTO aliYunConfigDTO, Map<String, Object> extendVars) {
        String regionId = aliYunConfigDTO.getRegionId();
        String sysDomain = aliYunConfigDTO.getSysDomain();
        String signName = aliYunConfigDTO.getSignName();
        String templateCode = extendVars.containsKey("TemplateCode")? extendVars.get("TemplateCode").toString() : aliYunConfigDTO.getTemplateCode();

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(sysDomain);
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", regionId);

        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);

        return request;
    }

    IAcsClient iAcsClient = null;

    private IAcsClient getIacsClient(AliYunConfigDTO aliYunConfigDTO) {
        if (ObjectUtil.isNotNull(iAcsClient)) {
            return iAcsClient;
        }

        String regionId = aliYunConfigDTO.getRegionId();
        String accessKeyId = aliYunConfigDTO.getAccessKeyId();
        String accessSecret = AESUtil.decrypt(aliYunConfigDTO.getAccessSecret());

        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        this.iAcsClient = client;
        return client;
    }


    @Override
    public String getTitle() {
        return "短信";
    }

    @Override
    public boolean getIsDefault() {
        return false;
    }

    @Override
    public boolean getSupportHtml() {
        return true;
    }

}
