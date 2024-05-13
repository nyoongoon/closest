package com.example.closest.domain.token.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 토큰 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; //TODO 외래키?

    private String tokenValue;

    @Builder
    public Token(String username, String tokenValue) {
        this.username = username;
        this.tokenValue = tokenValue;
    }

    public static Token of(String username, String tokenValue){
        return Token.builder()
                .username(username)
                .tokenValue(tokenValue)
                .build();
    }
}
