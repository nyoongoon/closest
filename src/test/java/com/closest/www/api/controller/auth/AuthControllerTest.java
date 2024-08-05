package com.closest.www.api.controller.auth;

import com.closest.www.api.service.auth.AuthService;
import com.closest.www.config.configuration.SecurityConfig;
import com.closest.www.config.filter.JwtAuthenticationFilter;
import com.closest.www.support.ControllerTestSupport;
import com.closest.www.support.mock.MockUser;
import com.closest.www.utility.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static com.closest.www.api.controller.auth.request.SignRequest.SignIn;
import static com.closest.www.api.controller.auth.request.SignRequest.SignUp;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * UserDetailService의 repository는 목으로 해볼 수 있지 않을까..?
 */
//@Import({SecurityConfig.class,
//        JwtAuthenticationFilter.class,
//        JwtTokenProvider.class})
//@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTestSupport {



    @DisplayName("이메일과 비밀번호, 확인 비밀번호로 회원가입을 신청 한다.")
    @Test
    void signup() throws Exception {
        //given
        SignUp signUp = new SignUp(
                "abcd",
                "abcd1234",
                "이름"
        );
        String json = objectMapper.writeValueAsString(signUp);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("이메일과 비밀번호로 로그인을 진행한다.")
    @Test
    void siginin() throws Exception {
        // given
        SignUp signUp = new SignUp(
                "abcd",
                "abcd1234",
                "이름"
        );

        authService.signup(signUp); //가입

        SignIn signIn = new SignIn(
                signUp.userEmail(),
                signUp.password()
        );

        String json = objectMapper.writeValueAsString(signIn);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists("accessToken"))
                .andExpect(MockMvcResultMatchers.cookie().exists("refreshToken"))
                .andDo(MockMvcResultHandlers.print());
    }


    @DisplayName("올바른 엑세스 토큰으로 성공 요청을 받는다.")
    @Test
    void requestWithRightAccessToken() throws Exception {
        // given
        SignUp signUp = new SignUp("abcd", "Abcdefg99!", "Abcdefg99!");
        authService.signup(signUp); //가입

        SignIn signIn = new SignIn(
                signUp.userEmail(),
                signUp.password()
        );
        String json = objectMapper.writeValueAsString(signIn);
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