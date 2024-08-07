package com.closest.www.api.controller.auth.request;

import com.closest.www.api.service.auth.exception.NotEqualPasswordsException;
import com.closest.www.api.service.auth.request.SignServiceRequest.SignInServiceRequest;
import com.closest.www.api.service.auth.request.SignServiceRequest.SignUpServiceRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.closest.www.utility.constant.Regex.PASSWORD;
import static com.closest.www.utility.constant.ValidationString.*;

public record SignRequest() {

    public record SignUpRequest(
            @NotBlank(message = EMAIL_IS_REQUIRED)
            @Email(message = NOT_VALID_EMAIL_FORM)
            String userEmail,

            @NotBlank(message = PASSWORD_IS_REQUIRED)
            @Pattern(regexp = PASSWORD, message = NOT_VALID_PASSWORD_FORM)
            @Size(min = 8, max = 64, message = NOT_VALID_PASSWORD_SIZE)
            String password,

            @NotBlank(message = CONFIRM_PASSWORD_IS_REQUIRED)
            @Pattern(regexp = PASSWORD, message = NOT_VALID_CONFIRM_PASSWORD_FORM)
            @Size(min = 8, max = 64, message = NOT_VALID_CONFIRM_PASSWORD_SIZE)
            String confirmPassword
    ) {
        public SignUpRequest {
            if (password != null && !password.equals(confirmPassword)) {
                throw new NotEqualPasswordsException();
            }
        }

        public SignUpServiceRequest toServiceRequest() {
            return SignUpServiceRequest.builder()
                    .userEmail(userEmail)
                    .password(password)
                    .confirmPassword(confirmPassword)
                    .build();
        }
    }

    public record SignInRequest(
            @NotBlank(message = EMAIL_IS_REQUIRED)
            @Email(message = NOT_VALID_EMAIL_FORM)
            String userEmail,

            @NotBlank(message = PASSWORD_IS_REQUIRED)
            @Pattern(regexp = PASSWORD, message = NOT_VALID_PASSWORD_FORM)
            @Size(min = 8, max = 64, message = NOT_VALID_PASSWORD_SIZE)
            String password
    ) {
        public SignInServiceRequest toServiceRequest() {
            return SignInServiceRequest.builder()
                    .userEmail(userEmail)
                    .password(password)
                    .build();
        }
    }
}
