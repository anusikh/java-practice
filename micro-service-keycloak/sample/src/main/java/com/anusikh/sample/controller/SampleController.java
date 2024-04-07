package com.anusikh.sample.controller;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping(value = "/user")
    public String getUser() {
        Jwt s = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String userId = "";

        Map<String, Object> customClaims = s.getClaims();
        if (customClaims.containsKey("sub")) {
            // to get other info, replace sub with name, email, preferred_username etc..
            userId = String.valueOf(customClaims.get("sub"));
        }

        return userId;
    }

    @GetMapping(value = "/admin")
    public String getAdmin() {
        return "admin";
    }
}
