package com.example.closest.domain.member;

import com.example.closest.common.exception.auth.IdNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Member 도메인 서비스 구현 클래스
 */
@Service
public class MemberDomainImpl implements MemberDomain, UserDetailsService {
    private final MemberRepository memberRepository;

    public MemberDomainImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        // Member 엔티티 UserDetails 상속하고 있음..
        // todo UsernameNotFoundException를  IdNotFoundException로 바꾸기..?
        return this.memberRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));
    }

    // 가입 -> Member 저장
    @Override
    @Transactional
    public void regist(Member member) {
        this.memberRepository.save(member);
    }

    @Override
    public boolean isMemberExists(String userEmail) {
        Optional<Member> member = this.memberRepository.findByUserEmail(userEmail);
        return member.isPresent();
    }

    @Override
    public Member findMemberByUserEmail(String userEmail) {
        return this.memberRepository.findByUserEmail(userEmail)
                .orElseThrow(()-> new IdNotFoundException());
    }
}
