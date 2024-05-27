package com.example.closest.common.dto;

import com.example.closest.common.exception.Authority;

import java.util.List;

/**
 * 인증 Dto
 */
public class AuthDto { //TODO Request vs Response 구분
    public static class SignIn {
        private String userEmail;
        private String password;

        protected SignIn() { //todo dto objectMapper 사용시 protected 기본생성자 필요..
        }

        private SignIn(Builder builder) {
            this.userEmail = builder.userEmail;
            this.password = builder.password;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public String getPassword() {
            return password;
        }

        public static final class Builder {
            private String userEmail;
            private String password;

            public Builder() {
            }

            public Builder userEmail(String userEmail) {
                this.userEmail = userEmail;
                return this;
            }

            public Builder password(String password) {
                this.password = password;
                return this;
            }

            public SignIn build(){
                return new SignIn(this);
            }
        }

    }

    public static class SignUp {
        private String userEmail;
        private String password;
        private List<Authority> roles;

        protected SignUp(){} //objectMapper 사용 시 필요

        private SignUp(Builder builder) {
            this.userEmail = builder.userEmail;
            this.password = builder.password;
            this.roles = builder.roles;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public String getPassword() {
            return password;
        }

        public List<Authority> getRoles() {
            return roles;
        }

        public static final class Builder {
            private String userEmail;
            private String password;
            private List<Authority> roles;

            public Builder() {
            }

            public Builder userEmail(String userEmail) {
                this.userEmail = userEmail;
                return this;
            }

            public Builder password(String password) {
                this.password = password;
                return this;
            }

            public Builder roles(List<Authority> roles) {
                this.roles = roles;
                return this;
            }

            public SignUp build() {
                return new SignUp(this);
            }
        }

    }
}
