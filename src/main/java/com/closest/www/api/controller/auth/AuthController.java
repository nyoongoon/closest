package com.closest.www.api.controller.auth;

import com.closest.www.api.ApiResponse;
import com.closest.www.api.controller.auth.request.SignRequest.SignInRequest;
import com.closest.www.api.controller.auth.request.SignRequest.SignUpRequest;
import com.closest.www.api.service.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
     * @param signUpRequest
     * @return
     */
    @PostMapping("/signup")
    public ApiResponse<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        this.authService.signup(signUpRequest.toServiceRequest());
        return ApiResponse.ok();
    }

    /**
     * 인증 및 토큰 리턴
     * <p>
     * 엑세스 토큰은 응답바디에, 리프레시 토큰은 쿠키에 저장
     *
     * @param signInRequest
     * @param response
     * @return
     */
    @PostMapping("/signin")
    public ApiResponse<Void> signin(@Valid @RequestBody SignInRequest signInRequest,
                                    HttpServletResponse response) {
        authService.signin(signInRequest.toServiceRequest(), response);
        return ApiResponse.ok();
    }

    @PreAuthorize("hasRole('READ')")
    @GetMapping("/refresh")
    public void refresh() {

    }
}
