package com.dstz.base.common.script;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.dstz.base.api.vo.ApiResponse;
import com.dstz.base.common.events.AbRequestLogEvent;
import com.dstz.base.common.requestlog.AbRequestLog;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 脚本执行日志
 *
 */
@Component
public class ScriptLogListener implements ApplicationListener<AbRequestLogEvent> {

	@Override
	public void onApplicationEvent(AbRequestLogEvent event) {
		if (AbRequestLogEvent.EventType.POST_PROCESS.equals(event.getEventType()) && ScriptLog.isOpenScriptLog()) {
			AbRequestLog requestLog = event.getRequestLog();
			// 如果存在异常，则在异常封装类设置脚本日志
			if(requestLog.getException() != null) return ;
			
			Object respones =  requestLog.getResponseBody();
			if(respones instanceof ApiResponse) {
				List<String> msgs = ScriptLog.getMsgsAndClear();
				// 设置返回值
				if(CollUtil.isNotEmpty(msgs)) {
					((ApiResponse<?>) respones).setScriptLog(StrUtil.join("\n", msgs));
				}
			}
		}
	}
}
