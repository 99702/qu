package com.qu.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class SecretController {
    @GetMapping("/secret")
    public String getSecret(){
        return "This is secret text";
    }
}
