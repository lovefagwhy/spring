package com.code.anno;

import java.lang.annotation.*;

/**
 * @author liuzhuang
 * @date 2018/12/13 10:01
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CodeAutowird {
    String value() default  "";
}
