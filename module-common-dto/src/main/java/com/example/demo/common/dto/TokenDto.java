package com.example.demo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토큰 Dto
 */
@Getter
@AllArgsConstructor
public class TokenDto { //TODO Request vs Response 구분
    private String username;
    private String accessToken;
    private String refreshToken;
}
