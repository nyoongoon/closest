package com.closest.www.learning.xss;

import com.closest.www.config.configuration.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class XssTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    // multipart/form-data 테스트
    @Test
    public void testMultipartFormData() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "file content".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/multipart")
                        .file(file)
                        .param("field", "<script>alert(1)</script>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.escapedFormData").value("{field=&lt;script&gt;alert(1)&lt;/script&gt;}"));
    }

    // application/x-www-form-urlencoded 테스트
    @Test
    public void testFormUrlEncoded() throws Exception {
        mockMvc.perform(post("/api/form-urlencoded")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("field=<script>alert(1)</script>"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.escapedFormData").value("{field=&lt;script&gt;alert(1)&lt;/script&gt;}"));
    }

    // text/plain 테스트
    @Test
    public void testPlainText() throws Exception {
        mockMvc.perform(post("/api/text")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("<script>alert(1)</script>"))
                .andExpect(status().isOk())
                .andExpect(content().string("Escaped text: &#60;script&#62;alert(1)&#60;/script&#62;"));
    }

    // application/json 테스트
    @Test
    public void testJson() throws Exception {
        String json = "{\"field\":\"<script>alert(1)</script>\"}";
        mockMvc.perform(post("/api/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.escapedJsonData.field").value("&#60;script&#62;alert(1)&#60;/script&#62;"));
    }

    @Test
    void postXss() throws Exception {
        //given
        String input = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";

        XssRequestDto xssRequestDto = new XssRequestDto(input);

        String json = objectMapper.writeValueAsString(xssRequestDto);
        //expected
        mockMvc.perform(post("/xss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(expectedOutput));
    }

    @Test
    void formXss() throws Exception {
        // given
        String input = "<li>content</li>";
        XssRequestDto content = new XssRequestDto(input);
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";

        // expected
        mockMvc.perform(post("/xss/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .content(objectMapper.writeValueAsString(content)))
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
        String input = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";
        LocalDate requestDate = LocalDate.of(2019,12,29);
        XssRequestLocalDateDto xssRequestLocalDateDto = new XssRequestLocalDateDto(input, requestDate);
        String json = objectMapper.writeValueAsString(xssRequestLocalDateDto);

        // expected
        mockMvc.perform(post("/xss/local-date")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(expectedOutput));
    }


    @Test
    void getXss() throws Exception {
        //given
        String input = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/xss")
                        .param("content", input))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(expectedOutput));
    }

    @Test
    void jsonXssProtectionTest() throws Exception {
        String input = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";;

        XssRequestDto xssRequestDto = new XssRequestDto(input);
        String json = objectMapper.writeValueAsString(xssRequestDto);

        mockMvc.perform(post("/json")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(expectedOutput));
    }

    @Test
    void formXssProtectionTest() throws Exception {
        String input = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";

        mockMvc.perform(post("/xss/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("content", input))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }

    @Test
    void uploadTest() throws Exception {
        String inputContent = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";

        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test content".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .param("content", inputContent))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }

    /**
     * 해당 컨트롤러는 선언하면 안됨 (파일과 dto 분리)
     * @throws Exception
     */
//    @Test
//    void multipartXssProtectionTest() throws Exception {
//        String input = "<li>content</li>";
//        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";
//
//        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, input.getBytes());
//
//        mockMvc.perform(MockMvcRequestBuilders.multipart("/multipart")
//                        .file(file))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(Arrays.toString(expectedOutput.getBytes())));
//    }

    @Test
    void plainTextXssProtectionTest() throws Exception {
        String input = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";

        mockMvc.perform(post("/plain")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(input))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }

    @Test
    void xmlXssProtectionTest() throws Exception {
        String input = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";

        String xmlContent = "<xssRequestDto><content>" + input + "</content></xssRequestDto>";

        mockMvc.perform(post("/xml")
                        .contentType(MediaType.APPLICATION_XML)
                        .content(xmlContent))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.xpath("//content").string(expectedOutput));
    }

    @Test
    void noContentTypeTest() throws Exception {
        String input = "<li>content</li>";

        mockMvc.perform(post("/xss")
                        .content(input)) // Content-Type 없이 전송
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnsupportedMediaType());
    }
    @Test
    void complexJsonXssTest() throws Exception {
        String input = "<li>content</li>";
        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";

        ComplexDto complexDto = new ComplexDto(new XssRequestDto(input), Arrays.asList(new XssRequestDto(input)));
        String json = objectMapper.writeValueAsString(complexDto);

        mockMvc.perform(post("/xss/complex")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mainContent.content").value(expectedOutput))
                .andExpect(jsonPath("$.subContents[0].content").value(expectedOutput));
    }

    // 1. 빈 문자열 테스트
    @Test
    void emptyStringTest() throws Exception {
        String input = "";
        String expectedOutput = "";

        mockMvc.perform(post("/xss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(expectedOutput));
    }

    // 2. null 값 테스트
    @Test
    void nullValueTest() throws Exception {
        mockMvc.perform(post("/xss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":null}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").doesNotExist());
    }

    // 3. 유효하지 않은 JSON 형식 테스트
    @Test
    void invalidJsonFormatTest() throws Exception {
        //String content = "<li>content</li>";
        //        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";
        String invalidJson = "{content: <li>content</li>}"; // 잘못된 JSON 형식

        mockMvc.perform(post("/xss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    // 4. 유효하지 않은 XML 형식 테스트
    @Test
    void invalidXmlFormatTest() throws Exception {
//        String content = "<li>content</li>";
//        String expectedOutput = "&lt;li&gt;content&lt;/li&gt;";
        String invalidXml = "<xssRequestDto><content><li>content</li></content>"; // 잘못된 XML 형식

        mockMvc.perform(post("/xml")
                        .contentType(MediaType.APPLICATION_XML)
                        .content(invalidXml))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    // 5. XSS 필터링 우회 시도 테스트
    @Test
    void xssBypassAttemptTest() throws Exception {
        String input = "<s<script>cript>alert(XSS)</s<script>cript>";
        String expectedOutput = "&lt;s&lt;script&gt;cript&gt;alert(XSS)&lt;/s&lt;script&gt;cript&gt;";

        mockMvc.perform(post("/xss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"" + input + "\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(expectedOutput));
    }

    // 6. Multipart empty content 테스트
    @Test
    void multipartEmptyContentTest() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.txt", MediaType.TEXT_PLAIN_VALUE, "".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/multipart")
                        .file(emptyFile))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    // 7. Multipart null content 테스트
    @Test
    void multipartNullContentTest() throws Exception {
        MockMultipartFile nullFile = new MockMultipartFile("file", "null.txt", MediaType.TEXT_PLAIN_VALUE, (byte[]) null);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/multipart")
                        .file(nullFile))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
