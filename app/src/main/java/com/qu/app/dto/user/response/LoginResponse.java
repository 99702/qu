package com.qu.app.dto.user.response;

import lombok.Data;

import java.time.Instant;

@Data
public class LoginResponse {
    private String name;
    private String email;
    private String token;
    private String refreshToken;
    private Instant expiresAt;
}