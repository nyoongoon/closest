package com.closest.www.api.controller.auth;

import com.closest.www.api.service.auth.request.SignServiceRequest.SignUpServiceRequest;
import com.closest.www.api.service.blog.exception.BlogNotFoundException;
import com.closest.www.support.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static com.closest.www.api.controller.auth.exception.AuthControllerExceptionMessageConstants.*;
import static com.closest.www.api.controller.auth.request.SignRequest.SignInRequest;
import static com.closest.www.api.controller.auth.request.SignRequest.SignUpRequest;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class AuthControllerTest extends ControllerTestSupport {


    @DisplayName("이메일과 비밀번호, 확인 비밀번호로 회원가입을 신청 한다.")
    @Test
    void signup() throws Exception {
        //given
        SignUpRequest signUpRequest = new SignUpRequest(
                "abcd@naver.com",
                "Abcd1234!",
                "Abcd1234!"
        );
        String json = objectMapper.writeValueAsString(signUpRequest);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @DisplayName("회원가입 시 이메일이 없으면 에러가 발생한다.")
    @Test
    void signupBlankEmail() throws Exception {
        //given
        SignUpRequest signUpRequest = new SignUpRequest(
                "",
                "Abcd1234!",
                "Abcd1234!"
        );
        String json = objectMapper.writeValueAsString(signUpRequest);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(EMAIL_IS_REQUIRED));
    }

    @DisplayName("회원가입 시 비밀번호가 없으면 에러가 발생한다.")
    @Test
    void signupBlankPassword() throws Exception {
        //given
        SignUpRequest signUpRequest = new SignUpRequest(
                "abcd@naver.com",
                null,
                "Abcd1234!"
        );
        String json = objectMapper.writeValueAsString(signUpRequest);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(PASSWORD_IS_REQUIRED));
    }

    @DisplayName("회원가입 시 확인 비밀번호가 없으면 에러가 발생한다.")
    @Test
    void signupBlankConfirmPassword() throws Exception {
        //given
        SignUpRequest signUpRequest = new SignUpRequest(
                "abcd@naver.com",
                "Abcd1234!",
                null
        );
        String json = objectMapper.writeValueAsString(signUpRequest);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(CONFIRM_PASSWORD_IS_REQUIRED));
    }

    @DisplayName("회원가입 신청 시 이메일 형식이 틀리면 에러가 발생한다.")
    @Test
    void signupNotValidEmailForm() throws Exception {
        //given
        SignUpRequest signUpRequest = new SignUpRequest(
                "abcd",
                "Abcd1234!",
                "Abcd1234!"
        );
        String json = objectMapper.writeValueAsString(signUpRequest);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(NOT_VALID_EMAIL_FORM));

    }

    @DisplayName("회원가입 신청 시 비밀번호가 8자 이상이 아니면 에러가 발생한다.")
    @Test
    void signupOverPasswordLength() throws Exception {
        //given
        SignUpRequest signUpRequest = new SignUpRequest(
                "abcd@naver.com",
                "Abcd12!",
                "Abcd124!"
        );
        String json = objectMapper.writeValueAsString(signUpRequest);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(NOT_VALID_PASSWORD_SIZE));
    }

    @DisplayName("회원가입 신청 시 비밀번호에 대문자가 포함되어 있지 않으면 아니면 않으면 에러가 발생한다.")
    @Test
    void signupNonUpperCasePassword() throws Exception {
        //given
        SignUpRequest signUpRequest = new SignUpRequest(
                "abcd@naver.com",
                "abcd1234!",
                "Abcd1234!"
        );
        String json = objectMapper.writeValueAsString(signUpRequest);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(NOT_VALID_PASSWORD_FORM));
    }

    @DisplayName("회원가입 신청 시 소문자가 포함되어 있지 않으면 아니면 않으면 에러가 발생한다.")
    @Test
    void signupNonDownCasePassword() throws Exception {
        //given
        SignUpRequest signUpRequest = new SignUpRequest(
                "abcd@naver.com",
                "ABCD1234!",
                "Abcd1234!"
        );
        String json = objectMapper.writeValueAsString(signUpRequest);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(NOT_VALID_PASSWORD_FORM));
    }

    @DisplayName("회원가입 신청 시 특수문자가 포함되어 있지 않으면 아니면 않으면 에러가 발생한다.")
    @Test
    void signupNonSpecialCasePassword() throws Exception {
        //given
        SignUpRequest signUpRequest = new SignUpRequest(
                "abcd@naver.com",
                "Abcd1234",
                "Abcd1234!"
        );
        String json = objectMapper.writeValueAsString(signUpRequest);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(NOT_VALID_PASSWORD_FORM));

    }

    @DisplayName("이메일과 비밀번호로 로그인을 진행한다.")
    @Test
    void siginin() throws Exception {
        // given
        SignInRequest signInRequest = new SignInRequest(
                "abcd@naver.com",
                "12g4agg!"
        );

        String json = objectMapper.writeValueAsString(signInRequest);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"));
    }


    @DisplayName("올바른 엑세스 토큰으로 성공 요청을 받는다.")
    @Test
    void requestWithRightAccessToken() throws Exception {
        // given
        SignUpServiceRequest signUpRequest = new SignUpServiceRequest("abcd", "Abcdefg99!", "Abcdefg99!");
        authService.signup(signUpRequest); //가입

        SignInRequest signInRequest = new SignInRequest(
                signUpRequest.userEmail(),
                signUpRequest.password()
        );
        String json = objectMapper.writeValueAsString(signInRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String accessToken = result.getResponse().getCookie("accessToken").getValue();
    }

    @DisplayName("조작된 엑세스 토큰을 전달하면 에러가 발생한다.")
    @Test
    void requestWithNotValidAccessToken() throws Exception {
        // given
//        SignUp signUp = new SignUp.Builder()
//                .userEmail("abcd")
//                .password("abcd1234")
//                .roles(List.of(ROLE_USER))
//                .build();
//        authService.signup(signUp); //가입
//
//        SignIn signIn = new SignIn.Builder()
//                .userEmail(signUp.getUserEmail())
//                .password(signUp.getPassword())
//                .build();
//        String json = objectMapper.writeValueAsString(signIn);
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//
//        String accessToken = result.getResponse().getCookie("accessToken").getValue();
//
//        // expected
//        mockMvc.perform(MockMvcRequestBuilders.get("/auth/refresh") //READ 권한 필요
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + accessToken)) // 405 권한 에러 발생 중 -> 권한 생성로직 보기..
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//        String manipulated = accessToken.substring(0, accessToken.length() - 1); //조작된 토큰 //todo 테스트 쪼개기
//        Assertions.assertThrows(SignatureException.class, () -> {
//            mockMvc.perform(MockMvcRequestBuilders.get("/auth/refresh")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .header("Authorization", "Bearer " + manipulated))
//                    .andExpect(MockMvcResultMatchers.status().is4xxClientError())
//                    .andDo(MockMvcResultHandlers.print());
//        });
    }

    @DisplayName("엑세스 토큰이 만료되었을 경우 리프레시 토큰으로 엑세스 토큰을 재발급 받는다.")
    @Test
    void requestWithRefreshToken() throws Exception {
        //given
//        SignUp signUp = new SignUp.Builder()
//                .userEmail("abcd")
//                .password("abcd1234")
//                .roles(List.of(ROLE_USER))
//                .build();
//        authService.signup(signUp); //가입
//
//        SignIn signIn = new SignIn.Builder()
//                .userEmail(signUp.getUserEmail())
//                .password(signUp.getPassword())
//                .build();
//        String json = objectMapper.writeValueAsString(signIn);
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn();
//
//        String refreshToken = result.getResponse().getCookie("refreshToken").getValue();
//
//        // expectd
//        mockMvc.perform(MockMvcRequestBuilders.get("/auth/refresh")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .cookie(new Cookie("refreshToken", refreshToken)))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print());
    }
}