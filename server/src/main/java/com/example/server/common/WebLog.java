package com.example.server.common;

import java.lang.annotation.*;

/**
 * 日志
 * @author LIN
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebLog {
    String description() default "";
}
