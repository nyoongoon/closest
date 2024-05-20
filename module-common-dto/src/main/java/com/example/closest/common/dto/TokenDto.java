package com.example.closest.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토큰 Dto
 */
@Getter
@AllArgsConstructor
public class TokenDto { //TODO Request vs Response 구분
    private String userEmail;
    private String accessToken;
    private String refreshToken;
}
