package com.qu.app.dto.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String mobile;
    private String password;
}