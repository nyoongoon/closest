package com.example.closest.auth.controller;

import com.example.closest.auth.service.AuthAppService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.closest.common.dto.AuthDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthAppService authAppService;

    /**
     * 회원가입
     *
     * @param signUp
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody AuthDto.SignUp signUp) {
        this.authAppService.signup(signUp);
        return ResponseEntity.ok().build();
    }

    /**
     * 인증 및 토큰 리턴
     * <p>
     * 엑세스 토큰은 응답바디에, 리프레시 토큰은 쿠키에 저장
     *
     * @param signIn
     * @param response
     * @return
     */
    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthDto.SignIn signIn,
                                 HttpServletResponse response) {
        authAppService.signin(signIn, response);
        return ResponseEntity.ok().build();
    }
}
