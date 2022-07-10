package com.qu.app.service;

import com.qu.app.dto.user.request.LoginRequest;
import com.qu.app.dto.user.response.LoginResponse;
import com.qu.app.dto.user.response.RegisterResponse;
import com.qu.app.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    RegisterResponse registerUser(User user);//, MultipartFile photoFile);
    LoginResponse loginUser(LoginRequest loginRequest);
}