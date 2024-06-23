package com.closest.www.application.auth.controller;

import com.closest.www.application.auth.service.AuthAppService;
import jakarta.servlet.http.HttpServletResponse;
import com.closest.www.common.dto.AuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthAppService authAppService;

    public AuthController(AuthAppService authAppService) {
        this.authAppService = authAppService;
    }

    /**
     * 회원가입
     *
     * @param signUp
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody AuthRequest.SignUp signUp) {
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
    public ResponseEntity signin(@RequestBody AuthRequest.SignIn signIn,
                                 HttpServletResponse response) {
        authAppService.signin(signIn, response);
        return ResponseEntity.ok().build();
    }
}
