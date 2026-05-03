package com.dstz.base.common.utils;

import java.util.Date;

/**
 * <pre>
 * 预留入口
 * </pre>
 * 
 * @date 2022年3月30日
 */
public class ThreadNowUtil {
	private ThreadNowUtil() {
		
	}

	public static Date getNow() {
		Date now = new Date();
		return now;
	}
}
