package com.example.closest.member_management;

import com.example.closest.config.MockUser;
import com.example.closest.domain.member.Member;
import com.example.closest.domain.member.MemberDomainImpl;
import com.example.closest.member_management.request.AddBlogRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MemberManagementControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberDomainImpl memberDomain;

    @Test
    @MockUser()
    @DisplayName("이메일과 링크로 유저에 블로그를 추가한다. ")
    void test1() throws Exception {
        // given
        String userEmail = "abc@naver.com";
        Member member = Member.builder()
                .userEmail(userEmail)
                .password("1234")
                .build();
        memberDomain.regist(member);
        String link = "https://goalinnext.tistory.com";

        AddBlogRequest request = AddBlogRequest.of(
                userEmail,
                link
        );

        String json = objectMapper.writeValueAsString(request);


        // expected
        mockMvc.perform(post("/member-management/blog")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }
}