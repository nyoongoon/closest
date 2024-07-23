package com.closest.www.api.controller.blog;

import com.closest.www.api.config.MockUser;
import com.closest.www.api.controller.blog.request.AddBlogRequest;
import com.closest.www.domain.member.Member;
import com.closest.www.domain.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class BlogControllerTest {
    private static final Logger log = LoggerFactory.getLogger(BlogControllerTest.class);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @MockUser()
    @DisplayName("이메일과 링크로 유저에 블로그를 추가한다. ")
    @Transactional
    void test1() throws Exception {
        // given
        String userEmail = "abc@naver.com";
        Member member = new Member.Builder()
                .userEmail(userEmail)
                .password("1234")
                .build();
        memberRepository.save(member);
        String link = "https://goalinnext.tistory.com";

        AddBlogRequest request = AddBlogRequest.of(
                userEmail,
                new URL(link)
        );

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/member-management/blog")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());

        URL url = new URL(link);
        Member foundMember = memberRepository.findByUserEmail(userEmail)
                .orElseThrow();
        foundMember.getSubscriptions().stream()
                .filter(e -> e.getBlog().getUrl().equals(url))
                .findAny()
                .orElseThrow();
    }
}