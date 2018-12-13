package com.code.anno;

import java.lang.annotation.*;

/**
 * @author liuzhuang
 * @date 2018/12/13 10:01
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CodeService {
    String value() default "";
}
