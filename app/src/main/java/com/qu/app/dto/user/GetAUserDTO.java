package com.qu.app.dto.user;

import lombok.Builder;


@Builder
public class GetAUserDTO {
    private String name;
    private String email;
    private String mobile;
    private String profilePic;
}