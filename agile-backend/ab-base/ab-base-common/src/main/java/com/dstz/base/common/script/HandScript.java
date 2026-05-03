package com.dstz.base.common.script;

/**
 * <pre>
 * 手写脚本
 * </pre>
 * 
 * @date 2022年6月23日
 */
public class HandScript implements java.io.Serializable{

	private static final long serialVersionUID = -6112747578142353989L;

	/**
	 * 脚本内容
	 */
	private String script;
	/**
	 * 脚本的描述
	 */
	private String desc;

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
