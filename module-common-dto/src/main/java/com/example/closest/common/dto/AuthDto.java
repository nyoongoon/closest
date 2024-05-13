package com.example.closest.common.dto;

import lombok.Builder;
import lombok.Getter;
import com.example.closest.common.exception.Authority;

import java.util.List;

/**
 * 인증 Dto
 */
public class AuthDto { //TODO Request vs Response 구분
    @Getter
    public static class SignIn {
        private String username;
        private String password;

        @Builder
        public SignIn(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Getter
    public static class SignUp {
        private String username;
        private String password;
        private List<Authority> roles;

        @Builder
        public SignUp(String username, String password, List<Authority> roles) {
            this.username = username;
            this.password = password;
            this.roles = roles;
        }


    }
}
