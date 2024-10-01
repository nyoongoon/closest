package com.closest.www.api.controller.exception;

/**
 * ControllerExceptionMessageConstants 로 전환 고민 중
 */
public class ControllerExceptionMessageConstants {
    /* 이메일 */
    public static final String EMAIL_IS_REQUIRED = "이메일은 필수값입니다.";
    public static final String NOT_VALID_EMAIL_FORM = "올바른 이메일 형식이 아닙니다.";

    /* 비밀번호 */
    public static final String PASSWORD_IS_REQUIRED = "비밀번호는 필수값입니다.";
    public static final String CONFIRM_PASSWORD_IS_REQUIRED = "확인 비밀번호는 필수값입니다.";
    public static final String NOT_VALID_PASSWORD_FORM = "비밀번호는 대소문자와 특수문자가 하나 이상 씩 포함되어야합니다.";
    public static final String NOT_VALID_CONFIRM_PASSWORD_FORM = "확인 비밀번호는 대소문자와 특수문자가 하나 이상 씩 포함되어야합니다.";
    public static final String NOT_VALID_PASSWORD_SIZE = "비밀번호는 최소 8자 이상 최대 64자 이하입니다";
    public static final String NOT_VALID_CONFIRM_PASSWORD_SIZE = "확인 비밀번호는 최소 8자 이상 최대 64자 이하입니다";
    public static final String NOT_EQUAL_PASSWORDS = "비밀번호와 확인 비밀번호가 다릅니다.";

    /* URL */
    public static final String URL_IS_REQUIRED = "URL은 필수값입니다.";
}
