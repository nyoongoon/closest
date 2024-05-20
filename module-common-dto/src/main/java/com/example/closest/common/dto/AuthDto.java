package com.example.closest.common.dto;

import com.example.closest.common.exception.Authority;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 인증 Dto
 */
public class AuthDto { //TODO Request vs Response 구분
    @Getter
    public static class SignIn {
        private String userEmail;
        private String password;

        @Builder
        public SignIn(String userEmail, String password) {
            this.userEmail = userEmail;
            this.password = password;
        }
    }

    @Getter
    public static class SignUp {
        private String userEmail;
        private String password;
        private List<Authority> roles;

        @Builder
        public SignUp(String userEmail, String password, List<Authority> roles) {
            this.userEmail = userEmail;
            this.password = password;
            this.roles = roles;
        }


    }
}
