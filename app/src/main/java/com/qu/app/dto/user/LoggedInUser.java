package com.qu.app.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoggedInUser {
    private Long userId;
    private boolean enabled;
    private String role;
    private String name;
    private String email;
    private String mobile;
    private String jwtToken;
}
