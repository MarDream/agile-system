package com.dstz.base.common.enums;
/**
 * <pre>
 * 目前基于：elementui的tag 5种类型
 * </pre>
 * @date 2023年12月26日
 */
public enum LabelCss {
	/**
	 * 蓝色
	 */
	PRIMARY("","主色调"),
	/**
	 * 绿色
	 */
	SUCCESS("success","成功"),
	/**
	 * 黄色
	 */
	WARNING("warning","警告"),
	/**
	 * 危险
	 */
	DANGER("danger","错误"),
	/**
	 * 信息
	 */
	INFO("info","信息"),
	;
	
	/**
	 * 键
	 */
	private final String key;

	/**
	 * 值
	 */
	private final String value;

	LabelCss(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

}
