package com.dstz.base.common.events;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.dstz.base.common.enums.ErrorLogLeve;
import com.dstz.base.common.utils.AbRequestUtils;
import com.dstz.base.common.utils.UserContextUtils;
import com.dstz.org.api.model.IUser;
import org.springframework.context.ApplicationEvent;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;

/**
 * @Name AbErrorLogEvent
 * @description: 系统错误日志事件
 * @date 2023/8/2414:29
 */
public class AbErrorLogEvent extends ApplicationEvent {
    private static final long serialVersionUID = -3131555628978461408L;

    private AbErrorLogEvent(AbErrorLog abErrorLog) {
        super(abErrorLog);
    }

    /**
     * 使用异常信息，当前用户信息，当前请求信息构建异常信息事件
     *
     * @param e 异常信息
     * @return 异常日志事件
     */
    public static AbErrorLogEvent createErrorLog(Throwable e, ErrorLogLeve errorLogLeve) {
        return new AbErrorLogEvent(new AbErrorLog(e,errorLogLeve));
    }


    /**
     * 使用者自定义错误信息
     *
     * @param abErrorLog 异常日志信息
     * @return 异常日志事件
     */
    public static AbErrorLogEvent createErrorLog(AbErrorLog abErrorLog) {
        return new AbErrorLogEvent(abErrorLog);
    }

    public AbErrorLog getErrorLog() {
        return (AbErrorLog) getSource();
    }

    public static class AbErrorLog {

        /**
         * 操作人信息,默认是当前用户
         */
        private IUser operatorUser;

        /**
         * 出错操作
         */
        private String action;
        /**
         * 出错异常
         */
        private Throwable exception;
        /**
         * 入参信息
         */
        private Map paramMap;

        /**
         * 入参信息
         */
        private Map heardMap;

        /**
         * 请求地址
         */
        private String ip;

        /**
         * 错误
         */
        private ErrorLogLeve errorLogLeve;


        public AbErrorLog() {
        }

        public AbErrorLog(Throwable exception,ErrorLogLeve errorLogLeve) {
            this.exception = exception;
            this.errorLogLeve = errorLogLeve;
            this.operatorUser = UserContextUtils.getUser().orElse(null);
            HttpServletRequest request = AbRequestUtils.getHttpServletRequest();
            if (request != null) {
                setRequestInfo(request);
            }
        }

        public AbErrorLog(Exception exception, String action, Map paramMap,ErrorLogLeve errorLogLeve) {
            this.action = action;
            this.exception = exception;
            this.operatorUser = UserContextUtils.getUser().orElse(null);
            this.paramMap = paramMap;
            this.errorLogLeve = errorLogLeve;
            HttpServletRequest request = AbRequestUtils.getHttpServletRequest();
            if (request != null) {
                setRequestInfo(request);
            }
        }

        public ErrorLogLeve getErrorLogLeve() {
            return errorLogLeve;
        }

        public void setErrorLogLeve(ErrorLogLeve errorLogLeve) {
            this.errorLogLeve = errorLogLeve;
        }

        private void setRequestInfo(HttpServletRequest request) {
            this.ip = ServletUtil.getClientIP(request);
            MultiValueMap<String, String> headerValueMap = new LinkedMultiValueMap<>();

            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                Enumeration<String> headerValue = request.getHeaders(headerName);
                while (headerValue.hasMoreElements()) {
                    headerValueMap.add(headerName, headerValue.nextElement());
                }
            }

            this.heardMap = headerValueMap;
            if (CollUtil.isEmpty(paramMap)) {
                paramMap = ServletUtil.getParamMap(request);
            }

            if (StrUtil.isEmpty(action)) {
                action = request.getServletPath();
            }
        }

        public Throwable getException() {
            return exception;
        }

        public void setException(Throwable exception) {
            this.exception = exception;
        }

        public IUser getOperatorUser() {
            if (operatorUser != null) {
                return operatorUser;
            }
            return UserContextUtils.getUser().orElse(null);
        }

        public void setOperatorUser(IUser operatorUser) {
            this.operatorUser = operatorUser;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Map getParamMap() {
            return paramMap;
        }

        public void setParamMap(Map paramMap) {
            this.paramMap = paramMap;
        }

        public Map getHeardMap() {
            return heardMap;
        }

        public void setHeardMap(Map heardMap) {
            this.heardMap = heardMap;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }

}
