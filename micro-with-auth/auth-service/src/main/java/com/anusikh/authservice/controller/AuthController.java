package com.anusikh.authservice.controller;

import com.anusikh.authservice.dao.AuthRequest;
import com.anusikh.authservice.dao.IdTokenRequest;
import com.anusikh.authservice.entity.UserInfo;
import com.anusikh.authservice.service.UserInfoUserDetailsService;
import com.anusikh.authservice.util.JwtUtil;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/au")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserInfoUserDetailsService userInfoUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired 
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/welcome")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String welcome(@RequestHeader(value = "sample", required = false) String sample) {
        rabbitTemplate.convertAndSend("sample_exchange", "key.anusikh", "hello");
        return sample;
    }

    @PostMapping("/new")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        return userInfoUserDetailsService.register(userInfo);
    }

    @PostMapping("/auth")
    public String authenticateGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtUtil.generateToken(authRequest.getUsername());
            return token;
        } else {
            throw new UsernameNotFoundException("invalid user request");
        }
    }

    @PostMapping("/oauth")
    public String oauthAndGetToken(@RequestBody IdTokenRequest idTokenRequest) {
        return userInfoUserDetailsService.loginOauthGoogle(idTokenRequest);
    }

}
