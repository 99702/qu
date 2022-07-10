package com.qu.app.dto.user.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateRequest {
    private String name;
    private LocalDate dob;
    private String email;
    private String mobile;
    private String password;
    private byte[] profilePic;


}
