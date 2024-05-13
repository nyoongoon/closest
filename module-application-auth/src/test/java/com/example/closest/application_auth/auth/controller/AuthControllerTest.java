package com.example.closest.application_auth.auth.controller;

import com.example.closest.application_auth.service.AuthAppService;
import com.example.closest.common.dto.AuthDto.SignIn;
import com.example.closest.common.dto.AuthDto.SignUp;
import com.example.closest.domain.member.MemberDomain;
import com.example.closest.domain.member.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import com.example.closest.common.exception.Authority;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static com.example.closest.common.exception.Authority.ROLE_READ;
import static com.example.closest.common.exception.Authority.ROLE_WRITE;

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

    private SignUp signUp = SignUp.builder()
            .username("abcd")
            .password("abcd1234")
            .roles(List.of(ROLE_READ, ROLE_WRITE))
            .build();

    @Test
    void 회원가입_테스트() throws Exception {
        String json = objectMapper.writeValueAsString(this.signUp);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Member member = memberDomain.findMemberByUsername(this.signUp.getUsername());

        assertThat(this.signUp.getUsername()).isEqualTo(member.getUserEmail());
        for (Authority authority : this.signUp.getRoles()) {
            assertThat(member.getRoles().contains(authority)).isTrue();
        }
    }

    @Test
    void 로그인_테스트() throws Exception {
        authAppService.signup(this.signUp); //가입

        SignIn signIn = SignIn.builder()
                .username(this.signUp.getUsername())
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
    void 엑세스_토큰_인증_테스트() throws Exception {
        authAppService.signup(this.signUp); //가입

        SignIn signIn = SignIn.builder()
                .username(this.signUp.getUsername())
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

        accessToken = accessToken.substring(1);
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void 리프레시_토큰_인증_테스트() throws Exception {
        authAppService.signup(this.signUp); //가입

        SignIn signIn = SignIn.builder()
                .username(this.signUp.getUsername())
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