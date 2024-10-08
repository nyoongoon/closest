package com.closest.www.api.service.auth;

import com.closest.www.api.controller.auth.request.TokenDto;
import com.closest.www.api.service.auth.exception.DuplicateIdException;
import com.closest.www.api.service.auth.exception.MemberNotFoundException;
import com.closest.www.api.service.auth.exception.NotEqualPasswordsException;
import com.closest.www.api.service.auth.request.SignServiceRequest.SignInServiceRequest;
import com.closest.www.api.service.auth.request.SignServiceRequest.SignUpServiceRequest;
import com.closest.www.domain.member.Member;
import com.closest.www.domain.member.MemberRepository;
import com.closest.www.domain.token.Token;
import com.closest.www.domain.token.TokenRepository;
import com.closest.www.utility.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.closest.www.domain.member.MemberAuthority.ROLE_USER;

/**
 * 인증 서비스 클래스
 */
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, MemberRepository memberRepository, TokenRepository tokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
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
            throw new NotEqualPasswordsException();
        }

        // 존재여부 판단
        boolean isExists = memberRepository.existsByUserEmail(request.userEmail());
        if (isExists) {
            throw new DuplicateIdException();
        }
        // 엔티티 변환
        Member member = Member.create(
                request.userEmail(),
                request.password(),
                List.of(ROLE_USER),
                passwordEncoder
        );
        // 등록
        memberRepository.save(member);
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
        Member member = memberRepository.findByUserEmail(signInServiceRequest.userEmail())
                .orElseThrow(MemberNotFoundException::new);
        // 패스워드 일치 여부
        boolean isMatches = passwordEncoder.matches(signInServiceRequest.password(), member.getPassword());
        if (!isMatches) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
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
