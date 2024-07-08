package com.closest.www.domain.member;

import org.springframework.stereotype.Service;

/**
 * Member 도메인 서비스 인터페이스
 */
@Service
public interface MemberDomain {
    void regist(Member member);
    boolean isMemberExists(String userEmail);
    Member findMemberByUserEmail(String userEmail);
}
