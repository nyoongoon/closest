package com.closest.www.domain.token;

import jakarta.persistence.*;

/**
 * 토큰 엔티티
 */
@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userEmail; //TODO 외래키?

    private String tokenValue;

    protected Token(){}

    private Token(String userEmail, String tokenValue) {
        this.userEmail = userEmail;
        this.tokenValue = tokenValue;
    }

    public static Token create(String userEmail, String tokenValue){
        return new Token(userEmail, tokenValue);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getTokenValue() {
        return tokenValue;
    }
}
