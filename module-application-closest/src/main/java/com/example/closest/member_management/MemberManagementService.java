package com.example.closest.member_management;

import com.example.closest.domain.member.MemberDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberManagementService {
    private final MemberDomain memberDomain;

    public void addBlog(String userEmail, String blogLink) {
//        memberDomain.findMemberByUsername()
    }
}
