package com.qu.app.dto;

import com.qu.app.entity.User;
import com.qu.app.utils.AES;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;



public class CustomUserDetails implements UserDetails {
    @Autowired
    private User user;

    @Autowired
    private AES aes;

    public CustomUserDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<SimpleGrantedAuthority> set = new HashSet<>();
        set.add(new SimpleGrantedAuthority(this.user.getRole()));
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
//        return aes.decryptText("AES",user.getPassword());
    }

    @Override
    public String getUsername() {
        return user.getEmail();
//        System.out.println("++++"+aes.encryptText("AES",user.getEmail())+"++++");
//        return aes.encryptText("AES",user.getEmail());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
