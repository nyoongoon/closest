package com.example.demo.application_auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 예시 컨트롤러
 */
@RestController
public class ExampleController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PreAuthorize("hasRole('READ')")
    @GetMapping("/anyone")
    public String anyone() {
        return "anyone";
    }

    @PreAuthorize("hasRole('WRITE')")
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
