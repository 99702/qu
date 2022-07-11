package com.qu.app.config;

import com.qu.app.enumeration.PathConstant;
import com.qu.app.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.file.Path;

@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    // WHITE_LIST_URLS are allowed without authentication
    private static final String[] WHITE_LIST_URLS = {
            "/auth/" + PathConstant.REGISTER_USER,
            "/auth/" + PathConstant.LOGIN_USER,
            "/post/" + PathConstant.ALL_POST,
            "/post/" + PathConstant.SEARCH_POST_BY,
            "/post/" + PathConstant.SEARCH_POST_BY + "/t",
            "/post/" + PathConstant.SEARCH_POST_BY + "/d",
            "/post/" + PathConstant.SEARCH_POST_BY + "/u",
            "/post/" + PathConstant.SEARCH_POST_BY + "/similar",
    };

    private static final String[] ADMIN_URLS = {
            "/user/"+ PathConstant.DELETE_USER,
            "/user/"+ PathConstant.UPDATE_USER,
            "/post/"+PathConstant.DELETE_POST,
    };


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .antMatchers(WHITE_LIST_URLS).permitAll()
                .antMatchers(ADMIN_URLS).hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(11);
    }
}
