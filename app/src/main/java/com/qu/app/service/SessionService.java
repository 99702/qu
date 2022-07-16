package com.qu.app.service;

import com.qu.app.dto.user.LoggedInUser;

public interface SessionService {
    /**
     * Service provides information of logged in user
     *
     * @return LoggedInUserDto
     */
    LoggedInUser loggedInUser();

    String email();

    Long mobile();

}
