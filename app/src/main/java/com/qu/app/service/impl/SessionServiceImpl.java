package com.qu.app.service.impl;

import com.qu.app.dto.user.LoggedInUser;
import com.qu.app.service.SessionService;
import com.qu.app.utils.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    AES aes;
    LoggedInUser loggedInUser = new LoggedInUser();
    private HttpServletRequest request;

    public void saveForSession(HttpServletRequest request) {
        loggedInUser.setUserId(null);
    }

    @Override
    public LoggedInUser loggedInUser() {
        return this.loggedInUser;
    }

    @Override
    public String email() {
        return aes.decryptText("AES", (String) this.request.getAttribute("email"));
    }

    @Override
    public Long mobile() {
        return Long.parseLong(aes.decryptText("AES", (String) this.request.getAttribute("mobile")));
    }
}
