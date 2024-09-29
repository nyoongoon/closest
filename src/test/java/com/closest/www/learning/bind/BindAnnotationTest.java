package com.closest.www.learning.bind;

import com.closest.www.support.ControllerTestSupport;
import com.closest.www.support.mock.MockUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class BindAnnotationTest{
//public class BindAnnotationTest extends ControllerTestSupport {
   @Autowired
   private MockMvc mockMvc;

    @Test
    public void testHandleFileUpload() throws Exception {
        // MockMultipartFile 객체 생성
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",                     // 파라미터 이름
                "test-file.png",            // 파일 이름
                MediaType.IMAGE_PNG_VALUE, // 파일의 콘텐츠 타입
                "This is the file content".getBytes() // 파일의 내용
        );

        // Multipart 요청을 사용하여 테스트 수행
        mockMvc.perform(MockMvcRequestBuilders.multipart("/submit")
                        .file(mockFile))
//                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()); // HTTP 상태 코드 200 확인
    }
}
