package com.dstz.sys.sdk;

/**
 * 翻译异常，只做提示性
 *
 */
public class FanYiException extends RuntimeException {

	private static final long serialVersionUID = 9061247177417282827L;

	public FanYiException(String message) {
		super(message, null, false, false);
	}
}
