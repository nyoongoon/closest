package com.closest.www.api.service.auth;

import com.closest.www.api.service.auth.exception.NotEqualPasswordsException;
import com.closest.www.api.service.auth.request.SignServiceRequest;
import com.closest.www.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.closest.www.api.service.auth.request.SignServiceRequest.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceTest extends IntegrationTestSupport {
    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("회원가입 시 비밀번호와 확인 비밀번호가 일치하지 않으면 에러가 발생한다.")
    void signupNotEqualPasswords(){
        //given
        SignUpServiceRequest request  = SignUpServiceRequest.builder()
                .userEmail("abc2@naver.com")
                .password("wbho112@")
                .password("Tbho112@")
                .build();
        // expected
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(NotEqualPasswordsException.class);
    }
}