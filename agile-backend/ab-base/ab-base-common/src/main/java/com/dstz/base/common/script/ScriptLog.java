package com.dstz.base.common.script;

import java.util.ArrayList;
import java.util.List;

import com.dstz.base.common.property.PropertyEnum;
import com.dstz.base.common.utils.ContextCleanUtils;

import cn.hutool.core.util.StrUtil;

/**
 * <pre>
 * groovy脚本运行过程中行为日志
 * </pre>
 * 
 * @date 2023年12月21日
 */
public class ScriptLog {
	/**
	 * 系统是否开启脚本日志
	 */
	private final static ThreadLocal<Boolean> configOpen = new ThreadLocal<>();
	/**
	 * 线程是否开启脚本日志
	 */
	private final static ThreadLocal<Boolean> threadOpen = new ThreadLocal<>();
	/**
	 * 日志列表
	 */
	private final static ThreadLocal<List<String>> msgs = new ThreadLocal<>();

	public static boolean isOpen() {
		return isOpenScriptLog() && Boolean.TRUE.equals(threadOpen.get());
	}
	
	public static boolean isOpenScriptLog() {
		if (configOpen.get() == null) {
			boolean b = PropertyEnum.SCRIPT_LOG.getPropertyValue(Boolean.class);// 从配置读
			configOpen.set(b);
		}
		
		return Boolean.TRUE.equals(configOpen.get());
	}
	
	
	static {
		ContextCleanUtils.register(ScriptLog::clear, ContextCleanUtils.Phase.values());
	}

	public static void setThreadOpen(boolean b) {
		threadOpen.set(b);
	}
	
	public static void setConfigOpen(boolean b) {
		configOpen.set(b);
	}
	
	public static void openThread() {
		clear();
		threadOpen.set(true);
	}
	
	/**
	 * <pre>
	 * 增加日志
	 * 参数支持hutool字符串格式化：ScriptLog.addMsg("代码","代码描述","a={},b={}", a, b)
	 * </pre>
	 * 
	 * @param code
	 *            代码
	 * @param codeDesc
	 *            代码描述
	 * @param desc
	 *            参数描述
	 * @param args
	 *            参数描述用到的hutool参数
	 */
	public static void addMsg(String code, String codeDesc, String desc, Object... args) {
		if (!isOpen()) {
			return;
		}
		if (msgs.get() == null) {
			msgs.set(new ArrayList<>());
		}
		String str = "{}|{}。{}";
		msgs.get().add(StrUtil.format(str, code, codeDesc, StrUtil.format(desc, args)));
	}

	public static List<String> getMsgs() {
		return msgs.get() == null ? new ArrayList<>() : msgs.get();
	}

	public static void clear() {
		configOpen.remove();
		threadOpen.remove();
		msgs.remove();
	}

	public static List<String> getMsgsAndClear() {
		List<String> list = getMsgs();
		clear();
		return list;
	}
}
