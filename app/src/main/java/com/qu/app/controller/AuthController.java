package com.qu.app.controller;

import com.qu.app.dto.user.request.LoginRequest;
import com.qu.app.dto.user.response.LoginResponse;
import com.qu.app.dto.user.response.RegisterResponse;
import com.qu.app.entity.User;
import com.qu.app.enumeration.PathConstant;
import com.qu.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    /**
     * signin the user
     * @param loginRequest
     * @return - LoginResponse
     */
    @PostMapping(PathConstant.LOGIN_USER)
    public LoginResponse loginUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request){return authService.loginUser(request, loginRequest);}

    /**
     * Register a user
     * @param user
     * @return - ResponseEntity<RegisterUserDTO>
     */

    @PostMapping(value=PathConstant.REGISTER_USER)
    public ResponseEntity<RegisterResponse> registerUser(@RequestPart User user, @RequestParam(name = "profilePic", required = false) MultipartFile profilePic){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(user, profilePic));
    }

}
