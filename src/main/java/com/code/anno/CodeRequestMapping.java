package com.code.anno;

import java.lang.annotation.*;

/**
 * @author liuzhuang
 * @date 2018/12/13 10:02
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CodeRequestMapping {
    String value() default "";
}
