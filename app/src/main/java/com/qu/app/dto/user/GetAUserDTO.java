package com.qu.app.dto.user;

import lombok.Builder;
import lombok.Data;


@Data
public class GetAUserDTO {
    private String name;
    private String email;
    private String mobile;
    private String profilePic;
}