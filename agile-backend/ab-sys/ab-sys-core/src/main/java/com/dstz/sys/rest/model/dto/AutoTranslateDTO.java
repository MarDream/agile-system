package com.dstz.sys.rest.model.dto;


import java.util.List;

import javax.validation.constraints.NotEmpty;

import cn.hutool.core.collection.ListUtil;

/**
 * 自动翻译
 *
 */
public class AutoTranslateDTO implements java.io.Serializable {

	private static final long serialVersionUID = -1866516373715389892L;

	/**
	 * 中文文本
	 */
	@NotEmpty(message = "翻译文本不能为空")
	private String chineseText;

	/**
	 * 翻译为目标语言
	 */
	@NotEmpty(message = "翻译目标语言不能为空")
	private List<String> toLanguages;
	
	public AutoTranslateDTO() {
		
	}
	
	public AutoTranslateDTO(String chineseText, String... toLanguages) {
		super();
		this.chineseText = chineseText;
		this.toLanguages = ListUtil.toLinkedList(toLanguages);
	}

	public String getChineseText() {
		return chineseText;
	}

	public void setChineseText(String chineseText) {
		this.chineseText = chineseText;
	}

	public List<String> getToLanguages() {
		return toLanguages;
	}

	public void setToLanguages(List<String> toLanguages) {
		this.toLanguages = toLanguages;
	}


}
