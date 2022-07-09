package com.qu.app.service;

import com.qu.app.dto.user.LoginRequest;
import com.qu.app.dto.user.LoginResponse;
import com.qu.app.dto.user.RegisterUserDTO;
import com.qu.app.entity.User;

public interface AuthService {
    RegisterUserDTO registerUser(User user);
    LoginResponse loginUser(LoginRequest loginRequest);
}