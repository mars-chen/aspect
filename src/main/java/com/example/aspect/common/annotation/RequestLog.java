package com.example.aspect.common.annotation;

import java.lang.annotation.*;

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestLog {
    boolean request() default true;
    boolean response() default true;
}
