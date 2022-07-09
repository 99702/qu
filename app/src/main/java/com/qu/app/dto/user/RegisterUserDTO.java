package com.qu.app.dto.user;


import com.qu.app.utils.AES;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;



@Data
public class RegisterUserDTO {
    private String name;
    private LocalDate dob;
    private String email;
    private String mobile;
}
