package com.closest.www.api.service.auth.request;

public record SignServiceRequest() {

    public record SignUpServiceRequest(
            String userEmail,
            String password,
            String confirmPassword
    ) {

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            String userEmail;
            String password;
            String confirmPassword;

            private Builder() {
            }

            public SignUpServiceRequest.Builder userEmail(String userEmail) {
                this.userEmail = userEmail;
                return this;
            }

            public SignUpServiceRequest.Builder password(String password) {
                this.password = password;
                return this;
            }

            public SignUpServiceRequest.Builder confirmPassword(String confirmPassword) {
                this.confirmPassword = confirmPassword;
                return this;
            }


            public SignUpServiceRequest build() {
                return new SignUpServiceRequest(userEmail, password, confirmPassword);
            }
        }
    }

    public record SignInServiceRequest(
            String userEmail,
            String password
    ) {
        public static SignInServiceRequest.Builder builder() {
            return new SignInServiceRequest.Builder();
        }

        public static final class Builder {
            String userEmail;
            String password;

            private Builder() {
            }

            public SignInServiceRequest.Builder userEmail(String userEmail) {
                this.userEmail = userEmail;
                return this;
            }

            public SignInServiceRequest.Builder password(String password) {
                this.password = password;
                return this;
            }

            public SignInServiceRequest build() {
                return new SignInServiceRequest(userEmail, password);
            }
        }
    }
}
