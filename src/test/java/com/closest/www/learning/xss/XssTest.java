package com.closest.www.learning.xss;

import com.closest.www.config.configuration.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
public class XssTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postXss() throws Exception {
        //given
        String input = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";

        XssRequestDto xssRequestDto = new XssRequestDto(input);

        String json = objectMapper.writeValueAsString(xssRequestDto);
        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/xss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content").value(expectedOutput));
    }

    @Test
    void formXss() throws Exception {
        // given
        String input = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/xss/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("content", input))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(MockMvcResultMatchers.content().string(expectedOutput));
    }

    /**
     * 실패 이유는 기존에 등록된 ObjectMapper가 jsonEscapeConverter에 등록되어 있지 않기 때문
     * -> Jackson2ObjectMapperBuilder 이용
     *
     * @throws Exception
     * @see AppConfig#objectMapper()
     */
    @Test
    void localDateXss() throws Exception {
        // given
        String input = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";
        LocalDate requestDate = LocalDate.of(2019,12,29);
        XssRequestLocalDateDto xssRequestLocalDateDto = new XssRequestLocalDateDto(input, requestDate);
        String json = objectMapper.writeValueAsString(xssRequestLocalDateDto);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/xss/local-date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content").value(expectedOutput));
    }


    @Test
    void getXss() throws Exception {
        //given
        String content = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/xss")
                        .param("content", content))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.content").value(expectedOutput));
    }

}
