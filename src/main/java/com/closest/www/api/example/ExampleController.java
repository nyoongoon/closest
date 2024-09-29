package com.closest.www.api.example;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ExampleController {
    @PostMapping("/submit")
    public String handleFileUpload(@ModelAttribute("file") MultipartFile file,
                                   @ModelAttribute("dto") Dto dto) {
        // 'file' 파라미터로 전달된 파일이 MultipartFile로 바인딩됩니다.
        return "result";
    }

    @PostMapping("/submit2")
    public String handleFileUpload2(@ModelAttribute("dto") Dto dto) {
        // 'file' 파라미터로 전달된 파일이 MultipartFile로 바인딩됩니다.
        return "result";
    }
}
