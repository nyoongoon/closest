package com.closest.www.api.service.auth;

import com.closest.www.api.controller.auth.request.TokenDto;
import com.closest.www.api.service.auth.request.SignServiceRequest.SignInServiceRequest;
import com.closest.www.api.service.auth.request.SignServiceRequest.SignUpServiceRequest;
import com.closest.www.domain.member.Member;
import com.closest.www.domain.member.MemberJpaRepository;
import com.closest.www.domain.token.Token;
import com.closest.www.domain.token.TokenRepository;
import com.closest.www.utility.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.closest.www.api.exception.ExceptionMessageConstants.*;
import static com.closest.www.domain.member.MemberAuthority.ROLE_USER;

/**
 * 인증 서비스 클래스
 */
@Service
public class AuthService {
    private final MemberJpaRepository memberExampleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberJpaRepository memberJpaRepository;
    private final TokenRepository tokenRepository;

    public AuthService(MemberJpaRepository memberExampleRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, MemberJpaRepository memberJpaRepository, TokenRepository tokenRepository) {
        this.memberExampleRepository = memberExampleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.memberJpaRepository = memberJpaRepository;
        this.tokenRepository = tokenRepository;
    }

    /**
     * 가입 서비스 로직
     *
     * @param request
     */
    @Transactional
    public void signup(SignUpServiceRequest request) {
        // 비밀번호 일치 판단
        if (request.password() != null && !request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException(NOT_EQUAL_PASSWORDS);
        }
        // 존재여부 판단
        boolean isExists = memberJpaRepository.existsByUserEmail(request.userEmail());
        if (isExists) {
            throw new IllegalArgumentException(DUPLICATED_ID);
        }
        // 엔티티 변환
        Member member = Member.create(
                request.userEmail(),
                request.password(),
                List.of(ROLE_USER),
                passwordEncoder
        );
        // 등록
        memberJpaRepository.save(member);
    }

    /**
     * 로그인 서비스 로직
     *
     * @param signInServiceRequest
     * @param response
     */
    @Transactional
    public void signin(SignInServiceRequest signInServiceRequest, HttpServletResponse response) {
        //멤버 조회
        Member member = memberJpaRepository.findByUserEmail(signInServiceRequest.userEmail())
                .orElseThrow(()-> new IllegalArgumentException(MEMBER_NOT_FOUND));

        // 패스워드 일치 여부
        boolean isMatches = passwordEncoder.matches(signInServiceRequest.password(), member.getPassword());
        if (!isMatches) {
            throw new IllegalArgumentException(WRONG_PASSWORD);
        }
        // 토큰 Dto 생성
        TokenDto tokenDto = jwtTokenProvider.getTokens(member.getUserEmail(), member.getRoles());
        // 리프레시 토큰 엔티티 생성 및 신규 저장
        Token refreshToken = Token.create(tokenDto.getUserEmail(), tokenDto.getRefreshToken());
        renewalToken(refreshToken);

        // 엑세스&리프레시 토큰 쿠키에 저장
        jwtTokenProvider.addAccessTokenToCookie(response, tokenDto.getAccessToken());
        jwtTokenProvider.addRefreshTokenToCookie(response, tokenDto.getRefreshToken());
    }

    private void renewalToken(Token token) {
        String userEmail = token.getUserEmail();
        boolean isExists = tokenRepository.findByUserEmail(userEmail).isPresent();
        if (isExists) { // userEmail으로 등록된 리프레시 토큰 존재하면 삭제
            tokenRepository.deleteByUserEmail(userEmail);
        }
        // 새로 생성
        tokenRepository.save(token);
    }
}
