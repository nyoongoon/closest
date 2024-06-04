package com.closest.www.application.auth.service;

import com.closest.www.common.dto.AuthDto;
import com.closest.www.common.dto.TokenDto;
import com.closest.www.application.auth.DuplicateIdException;
import com.closest.www.domain.member.MemberDomain;
import com.closest.www.domain.member.Member;
import com.closest.www.domain.token.TokenDomain;
import com.closest.www.domain.token.entity.Token;
import com.closest.www.utility.token.TokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 서비스 클래스
 */
@Service
public class AuthAppService {

    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder; //todo 도메인 영역으로 옮길 수 있을지?
    private final MemberDomain memberDomain;
    private final TokenDomain tokenDomain;

    public AuthAppService(TokenProvider tokenProvider, PasswordEncoder passwordEncoder, MemberDomain memberDomain, TokenDomain tokenDomain) {
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.memberDomain = memberDomain;
        this.tokenDomain = tokenDomain;
    }

    /**
     * 가입 서비스 로직
     *
     * @param signUp
     */
    @Transactional
    public void signup(AuthDto.SignUp signUp) {
        // 존재여부 판단
        boolean isExists = memberDomain.isMemberExists(signUp.getUserEmail());
        if (isExists) {
            throw new DuplicateIdException();
        }
        // 패스워드 암호화
        String encodedPassword = this.passwordEncoder.encode(signUp.getPassword());
        // 엔티티 변환
        Member member = Member.of(signUp.getUserEmail(), encodedPassword, signUp.getRoles());
        // 등록
        memberDomain.regist(member);
    }

    /**
     * 로그인 서비스 로직
     *
     * @param signIn
     * @param response
     */
    @Transactional
    public void signin(AuthDto.SignIn signIn, HttpServletResponse response) {
        //멤버 조회
        Member member = this.memberDomain.findMemberByUserEmail(signIn.getUserEmail());
        // 패스워드 일치 여부
        boolean isMatches = this.isPasswordMatches(signIn.getPassword(), member.getPassword());
        if (!isMatches) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 토큰 Dto 생성
        TokenDto tokenDto = this.tokenProvider.getTokens(member.getUserEmail(), member.getRoles());
        // 리프레시 토큰 엔티티 생성 및 신규 저장
        Token refreshToken = Token.of(tokenDto.getUserEmail(), tokenDto.getRefreshToken());
        this.tokenDomain.renewalToken(refreshToken);

        // 엑세스&리프레시 토큰 쿠키에 저장
        this.tokenProvider.addAccessTokenToCookie(response, tokenDto.getAccessToken());
        this.tokenProvider.addRefreshTokenToCookie(response, tokenDto.getRefreshToken());
    }

    private boolean isPasswordMatches(String originPassword, String encryptedPassword) {
        return this.passwordEncoder.matches(originPassword, encryptedPassword);
    }
}