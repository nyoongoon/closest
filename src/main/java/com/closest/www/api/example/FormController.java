package com.closest.www.api.example;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/submit")
public class FormController {

    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public String submitForm(@ModelAttribute UserForm userForm) {


        // 실제로는 비즈니스 로직을 처리한 후 결과를 반환합니다.
        return "Form submitted successfully!";
    }
}