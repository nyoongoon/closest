package com.closest.www.api.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContext.class)
public @interface MockUser {
    String name() default "여름";

    String email() default "summer@naver.com";

    String password() default "1234";
}
