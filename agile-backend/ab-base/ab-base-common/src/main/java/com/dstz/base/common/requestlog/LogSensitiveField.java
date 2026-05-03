package com.dstz.base.common.requestlog;

import java.lang.annotation.*;

/**
 * 日志脱敏字段
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogSensitiveField {

}
