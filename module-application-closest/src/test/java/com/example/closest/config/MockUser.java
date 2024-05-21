package com.example.closest.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = HodologMockSecurityContext.class)
public @interface MockUser {
    String name() default "여름";

    String email() default "summer@naver.com";

    String password() default "";
}
