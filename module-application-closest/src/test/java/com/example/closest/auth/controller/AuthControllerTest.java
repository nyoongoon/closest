package com.example.closest.auth.controller;

import com.example.closest.auth.service.AuthAppService;
import com.example.closest.common.dto.AuthDto.SignIn;
import com.example.closest.common.dto.AuthDto.SignUp;
import com.example.closest.common.exception.Authority;
import com.example.closest.domain.member.Member;
import com.example.closest.domain.member.MemberDomain;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.closest.common.exception.Authority.ROLE_READ;
import static com.example.closest.common.exception.Authority.ROLE_WRITE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberDomain memberDomain;
    @Autowired
    private AuthAppService authAppService;

    private SignUp signUp = new SignUp.Builder()
            .userEmail("abcd")
            .password("abcd1234")
            .roles(List.of(ROLE_READ, ROLE_WRITE))
            .build();

    @Test
    @Transactional
    void 회원가입_테스트() throws Exception {
        String json = objectMapper.writeValueAsString(this.signUp);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Member member = memberDomain.findMemberByUserEmail(this.signUp.getUserEmail());

        assertThat(this.signUp.getUserEmail()).isEqualTo(member.getUserEmail());
        for (Authority authority : this.signUp.getRoles()) {
            assertThat(member.getRoles().contains(authority)).isTrue();
        }
    }

    @Test
    @Transactional
    void 로그인_테스트() throws Exception {
        authAppService.signup(this.signUp); //가입

        SignIn signIn = new SignIn.Builder()
                .userEmail(this.signUp.getUserEmail())
                .password(this.signUp.getPassword())
                .build();
        String json = objectMapper.writeValueAsString(signIn);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().exists("accessToken"))
                .andExpect(MockMvcResultMatchers.cookie().exists("refreshToken"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    void 엑세스_토큰_인증_테스트() throws Exception {
        authAppService.signup(this.signUp); //가입

        SignIn signIn = new SignIn.Builder()
                .userEmail(this.signUp.getUserEmail())
                .password(this.signUp.getPassword())
                .build();
        String json = objectMapper.writeValueAsString(signIn);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String accessToken = result.getResponse().getCookie("accessToken").getValue();

        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        String manipulated = accessToken.substring(0, accessToken.length() - 1); //조작된 토큰
        Assertions.assertThrows(SignatureException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + manipulated))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                    .andDo(MockMvcResultHandlers.print());
        });
    }

    @Test
    @Transactional
    void 리프레시_토큰_인증_테스트() throws Exception {
        authAppService.signup(this.signUp); //가입

        SignIn signIn = new SignIn.Builder()
                .userEmail(this.signUp.getUserEmail())
                .password(this.signUp.getPassword())
                .build();
        String json = objectMapper.writeValueAsString(signIn);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String refreshToken = result.getResponse().getCookie("refreshToken").getValue();
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}