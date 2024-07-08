package com.closest.www.api.controller.auth;

import com.closest.www.api.service.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import com.closest.www.api.controller.auth.request.AuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 회원가입
     *
     * @param signUp
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody AuthRequest.SignUp signUp) {
        this.authService.signup(signUp);
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
        authService.signin(signIn, response);
        return ResponseEntity.ok().build();
    }
}
