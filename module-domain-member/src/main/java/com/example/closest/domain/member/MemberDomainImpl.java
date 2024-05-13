package com.example.closest.domain.member;

import com.example.closest.common.exception.auth.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Member 도메인 서비스 구현 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDomainImpl implements MemberDomain, UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Member 엔티티 UserDetails 상속하고 있음..
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));
    }

    // 가입 -> Member 저장
    @Override
    @Transactional
    public void regist(Member member) {
        this.memberRepository.save(member);
    }

    @Override
    public boolean isMemberExists(String username) {
        Optional<Member> member = this.memberRepository.findByUsername(username);
        return member.isPresent();
    }

    @Override
    public Member findMemberByUsername(String username) {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(()-> new IdNotFoundException());
    }
}
