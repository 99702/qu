package com.qu.app.service;

import com.qu.app.dto.user.request.LoginRequest;
import com.qu.app.dto.user.response.LoginResponse;
import com.qu.app.dto.user.response.RegisterResponse;
import com.qu.app.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    /**
     * Register the user , given user details
     *
     * @param user
     * @return RegisterResponse dto
     */
    RegisterResponse registerUser(User user, MultipartFile photoFile);

    /**
     * For logging in the user, sends token and other details
     *
     * @param request
     * @param loginRequest
     * @return LoginResponse dto
     */
    LoginResponse loginUser(HttpServletRequest request, LoginRequest loginRequest);
}