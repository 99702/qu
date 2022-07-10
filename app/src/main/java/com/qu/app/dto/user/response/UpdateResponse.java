package com.qu.app.dto.user.response;

import lombok.Data;

import java.time.LocalDate;


@Data
public class UpdateResponse {
    private String name;
    private LocalDate dob;
    private String email;
    private String mobile;
    private byte[] profilePic;
}
