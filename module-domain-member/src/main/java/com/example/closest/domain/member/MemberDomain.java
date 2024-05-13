package com.example.closest.domain.member;

/**
 * Member 도메인 서비스 인터페이스
 */
public interface MemberDomain {
    void regist(Member member);
    boolean isMemberExists(String username);
    Member findMemberByUsername(String username);
}
