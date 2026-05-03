package com.dstz.base.common.freemark;

import java.io.IOException;

import freemarker.template.TemplateException;
/**
 * <pre>
 * Freemarker接口 定义
 * 日期:2022-01-02
 * </pre>
 */
public interface IFreemarkerEngine {

    /**
     * 根据字符串模版解析出内容
     *
     * @param templateSource 字符串模版。
     * @param model          环境参数。
     * @return 解析后的文本
     * @throws TemplateException
     * @throws IOException
     */
    String parseByString(String templateSource, Object model);

}