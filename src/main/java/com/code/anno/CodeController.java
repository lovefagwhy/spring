package com.code.anno;

import java.lang.annotation.*;

/**
 * @author liuzhuang
 * @date 2018/12/13 9:54
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CodeController {
    String value() default "";
}
