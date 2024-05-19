package com.anusikh.sample.controller;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anusikh.sample.dto.User;

@RestController
@RequestMapping("/sample")
public class SampleController {

    @GetMapping(value = "/user")
    public User getUser() {
        Jwt s = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Map<String, Object> customClaims = s.getClaims();
        User user = new User();
        if (customClaims.containsKey("sub")) {
            // to get other info, replace sub with name, email, preferred_username etc..
            user.setUserId(String.valueOf(customClaims.get("sub")));
            user.setUsername(String.valueOf(customClaims.get("preferred_username")));
            user.setEmail(String.valueOf(customClaims.get("name")));
        }

        return user;
    }

    @GetMapping(value = "/admin")
    public String getAdmin() {
        return "admin";
    }
}
