package com.closest.www.support.mock;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MockSecurityContext.class)
public @interface MockUser {
    String name() default "목유저명";

    String email() default "mock@naver.com";

    String password() default "1234";
}
