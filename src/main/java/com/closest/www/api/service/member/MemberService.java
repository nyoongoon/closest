package com.closest.www.api.service.member;

import com.closest.www.config.configuration.CustomUserDetailService;
import com.closest.www.domain.member.MemberRepository;
import org.springframework.stereotype.Service;

/**
 * Member 도메인 서비스 구현 클래스
 */
@Service
public class MemberService extends CustomUserDetailService { // todo..
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        super(memberRepository);
        this.memberRepository = memberRepository;
    }
}
