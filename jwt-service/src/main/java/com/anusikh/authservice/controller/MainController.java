package com.anusikh.authservice.controller;

import com.anusikh.authservice.dto.AuthRequest;
import com.anusikh.authservice.dto.IdTokenRequest;
import com.anusikh.authservice.entity.UserInfo;
import com.anusikh.authservice.repository.UserInfoRepository;
import com.anusikh.authservice.service.UserInfoUserDetailsService;
import com.anusikh.authservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class MainController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserInfoUserDetailsService userInfoUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    // use this annotation for role based endpoints
    // @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String welcome() {
        return "Welcome this endpoint secure";
    }

    @PostMapping("/new")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfoRepository.save(userInfo);
        return "done";
    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest,
            HttpServletResponse httpServletResponse) {
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtUtil.generateToken(authRequest.getUsername());

            // setting the cookie with the access token value
            final ResponseCookie responseCookie = ResponseCookie.from("AUTH-TOKEN", token)
                    .httpOnly(true)
                    .maxAge(7 * 24 * 3600)
                    .path("/")
                    .secure(false)
                    .build();
            httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

            return token;
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @PostMapping("/oauth")
    public String oauthAndGetToken(@RequestBody IdTokenRequest idTokenRequest,
            HttpServletResponse httpServletResponse) {

        String token = userInfoUserDetailsService.loginOauthGoogle(idTokenRequest);

        // setting the cookie with the access token value
        final ResponseCookie responseCookie = ResponseCookie.from("AUTH-TOKEN", token)
                .httpOnly(true)
                .maxAge(7 * 24 * 3600)
                .path("/")
                .secure(false)
                .build();

        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return token;
    }

}
