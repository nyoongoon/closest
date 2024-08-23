package com.closest.www.learning.xss;

import com.closest.www.config.configuration.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class XssFilterTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("multipart/form-data 문자열 이스케이프 테스트 - @RequestParam 문자열")
    @Test
    public void testMultipartFormData() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "file content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/multipart")
                        .file(file)
                        .param("content", "<script>alert(1)</script>")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.escapedFormData").value("{field=&lt;script&gt;alert&#40;1&#41;&lt;/script&gt;}"));
    }

    @DisplayName("multipart/form-data 문자열 이스케이프 테스트 - @RequestParam DTO")
    @Test
    public void testMultipartFormDataObj() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "multipart/form-data", "file content".getBytes());
//        XssRequestDto dto = new XssRequestDto("<script>alert(1)</script>");
        String json = "{\"content\":\"<script>alert(1)</script>\"}";
//        MockMultipartFile content = new MockMultipartFile("content", null, "application/json", json.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/multipart/object")
                        .file(file)
                        .param("content", json)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.escapedFormData").value("{field={\"content\":\"&lt;script&gt;alert&#40;1&#41;&lt;/script&gt;\"}}"));
    }

    @DisplayName("multipart/form-data 문자열 이스케이프 테스트 - @RequestPart 문자열")
    @Test
    public void testMultipartFormDataByRequestPart() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "file content".getBytes());

        MockMultipartFile content = new MockMultipartFile("content", null, "text/plain", "<script>alert(1)</script>".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/multipart/part")
                        .file(file)
                        .file(content)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.escapedFormData").value("{field=&lt;script&gt;alert&#40;1&#41;&lt;/script&gt;}"));
    }

    @DisplayName("multipart/form-data 문자열 이스케이프 테스트 - @RequestPart DTO")
    @Test
    public void testMultipartFormDataObjByRequestPart() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "multipart/form-data", "file content".getBytes());
        XssRequestDto dto = new XssRequestDto("<script>alert(1)</script>");
        String json = objectMapper.writeValueAsString(dto);
        MockMultipartFile content = new MockMultipartFile("content", null, "application/json", json.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/multipart/object/part")
                        .file(file)
                        .file(content)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.escapedFormData").value("{field=&lt;script&gt;alert&#40;1&#41;&lt;/script&gt;}"));
    }

    @DisplayName("application/x-www-form-urlencoded 문자열 이스케이프 테스트 - @RequestBody MultiValuMap")
    @Test
    public void testHandleFormUrlEncoded() throws Exception {
        this.mockMvc.perform(post("/api/form-urlencoded")
                        .content("field=<script>alert(1)</script>&content=<script>alert(1)</script>")
                        .contentType("application/x-www-form-urlencoded")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("&lt;script&gt;alert&#40;1&#41;&lt;/script&gt;"));
    }

    @DisplayName("application/x-www-form-urlencoded 문자열 이스케이프 테스트 - @RequestParam")
    @Test
    public void testHandleFormUrlEncodedByParam() throws Exception {
        this.mockMvc.perform(post("/api/form-urlencoded/param")
                        .param("field", "<script>alert(1)</script>")
                        .param("content", "<script>alert(1)</script>")
                        .contentType("application/x-www-form-urlencoded")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("&lt;script&gt;alert&#40;1&#41;&lt;/script&gt;"));
    }


    @DisplayName("text/plain 문자열 이스케이프 테스트")
    @Test
    public void testPlainText() throws Exception {
        mockMvc.perform(post("/api/text")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("<script>alert(1)</script>")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Escaped text: &lt;script&gt;alert&#40;1&#41;&lt;/script&gt;"));
    }

    @DisplayName("application/json 문자열 이스케이프 테스트")
    @Test
    public void testJson() throws Exception {
        XssRequestDto dto = new XssRequestDto("<script>alert(1)</script>");
        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("&lt;script&gt;alert&#40;1&#41;&lt;/script&gt;"));
    }

    /**
     * APPLICATION_FORM_URLENCODED_VALUE - param 요청
     *
     * @throws Exception
     */
    @Test
    void formXss() throws Exception {
        // given
        String input = "<script>alert(1)</script>";
        String expectedOutput = "&lt;script&gt;alert&#40;1&#41;&lt;/script&gt;";

        // expected
        mockMvc.perform(post("/xss/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("content", input)
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string(expectedOutput));
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
        String input = "<script>alert(1)</script>";
        LocalDate requestDate = LocalDate.of(2019, 12, 29);
        XssRequestLocalDateDto xssRequestLocalDateDto = new XssRequestLocalDateDto(input, requestDate);
        String json = objectMapper.writeValueAsString(xssRequestLocalDateDto);

        // expected
        mockMvc.perform(post("/xss/local-date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("&lt;script&gt;alert&#40;1&#41;&lt;/script&gt;"));
    }
}
