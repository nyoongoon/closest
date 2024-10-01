package com.closest.www.api.controller.blog;

import com.closest.www.api.controller.blog.request.AddBlogRequest;
import com.closest.www.support.ControllerTestSupport;
import com.closest.www.support.mock.MockUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class BlogControllerTest extends ControllerTestSupport {

    @DisplayName("이메일과 링크로 유저에 블로그 추가 요청을 한다")
    @Test
    @MockUser()
    void addBlog() throws Exception {
        // given
        String userEmail = "abc@naver.com";
        String url = "https://goalinnext.tistory.com";

        AddBlogRequest request = new AddBlogRequest(
                userEmail,
                new URL(url)
        );

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/blog/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("블로그 추가 시 이메일이 null이면 에러가 발생한다.")
    @Test
    @MockUser()
    void addBlogWithNullEmail() throws Exception {
        // given
        String userEmail = null;
        String url = "https://goalinnext.tistory.com";

        AddBlogRequest request = new AddBlogRequest(
                userEmail,
                new URL(url)
        );

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/blog/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이메일은 필수값입니다."));
    }

    /**
     * 왜 안되지..? -> todo @Email이 정규식 안태우는지 확인..?
     */
    @DisplayName("블로그 추가 시 이메일 형식이 다르면 에러가 발생한다.")
    @Test
    @MockUser()
    void addBlogWithWrongEmail() throws Exception {
        // given
        String json = "{\"userEmail\": \"invalid-email\", \"url\": \"http://valid-url.com\"}";

        // expected
        mockMvc.perform(post("/blog/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("올바른 이메일 형식이 아닙니다."));
    }


    @DisplayName("블로그 추가 시 url이 null이면 에러가 발생한다.")
    @Test
    @MockUser()
    void addBlogWithNullUrl() throws Exception {
        // given
        String json = "{\"userEmail\" : \"abc@naver.com\", \"url\" : null }";

        // expected
        mockMvc.perform(post("/blog/add")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("URL은 필수값입니다."));
    }
}