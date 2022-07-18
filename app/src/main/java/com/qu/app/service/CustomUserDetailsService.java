package com.qu.app.service;

import com.qu.app.dto.CustomUserDetails;
import com.qu.app.entity.User;
import com.qu.app.error.QuException;
import com.qu.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    //username as email
    public UserDetails loadUserByUsername(String username) {
        try {
            User user = userRepository.fetchByEmailExact(username);
            if (user == null) {
                throw new QuException("User doesn't exist");
            }
            return new CustomUserDetails(user);
        } catch (Exception e) {
            throw new QuException(e.getMessage());
        }
    }
}