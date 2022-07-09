package com.qu.app.controller;

import com.qu.app.dto.user.LoginRequest;
import com.qu.app.dto.user.LoginResponse;
import com.qu.app.dto.user.RegisterUserDTO;
import com.qu.app.entity.User;
import com.qu.app.enumeration.PathConstant;
import com.qu.app.service.AuthService;
import com.qu.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public LoginResponse loginUser(@RequestBody LoginRequest loginRequest){return authService.loginUser(loginRequest);};

    /**
     * Register a user
     * @param user
     * @return - ResponseEntity<RegisterUserDTO>
     */
    @PostMapping(PathConstant.REGISTER_USER)
    public ResponseEntity<RegisterUserDTO> registerUser(@RequestBody User user){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authService.registerUser(user));
    }
}
