package com.qu.app.service.impl;

import com.qu.app.dto.user.LoggedInUser;
import com.qu.app.service.SessionService;
import com.qu.app.utils.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SessionServiceImpl implements SessionService {
    private HttpServletRequest request;

    @Autowired
    AES aes;

    LoggedInUser loggedInUser = new LoggedInUser();

    public void saveForSession(HttpServletRequest request) {
        loggedInUser.setUserId(null);
    }


    public  SessionServiceImpl(HttpServletRequest request){
        this.request = request;
    }
    @Override
    public LoggedInUser loggedInUser() {
//        LoggedInUser loggedInUser = LoggedInUser.builder()
//                .name((String) this.request.getAttribute("name"))
//                .email((String) this.request.getAttribute("email"))
//                .role((String) this.request.getAttribute("role"))
//                .mobile((String) this.request.getAttribute("mobile"))
//                .enabled((boolean) this.request.getAttribute("enabled"))
//                .jwtToken((String) this.request.getAttribute("jwtToken"))
////                .userId()
//                .build();
        return this.loggedInUser;
    }

    @Override
    public String email() {
        return aes.decryptText("AES" ,(String) this.request.getAttribute("email"));
    }

    @Override
    public Long mobile() {
        return Long.parseLong(aes.decryptText("AES" ,(String) this.request.getAttribute("mobile")));
    }
}
