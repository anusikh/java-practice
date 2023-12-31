package com.example.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.dto.LoginDto;
import com.example.app.entity.UserInfo;
import com.example.app.repository.UserInfoRepository;
import com.example.app.util.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/login")
    public String login(@ModelAttribute("login") LoginDto loginDto) {
        // we are basically specifying the path of the template html file
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("login") LoginDto loginDto, BindingResult bindingResult, Model model,
            HttpServletResponse httpServletResponse) {

        try {
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            if (authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(loginDto.getUsername());
                final ResponseCookie responseCookie = ResponseCookie.from("AUTH_TOKEN", token)
                        .httpOnly(true)
                        .maxAge(7 * 24 * 3600)
                        .path("/")
                        .secure(false)
                        .build();
                httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
                // redirect to sample path (home page)
                return "redirect:/sample";
            }
            return "auth/login";
        } catch (Exception e) {
            System.out.println("Exception");
            model.addAttribute("loginSuccess", false);
            return "auth/login";
        }
    }

    @GetMapping("/sample")
    public String sample() {
        return "sample";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute("user") UserInfo userInfo) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserInfo userInfo, BindingResult bindingResult, Model model) {
        if (!userInfoRepository.existsByEmail(userInfo.getEmail())) {
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            userInfoRepository.save(userInfo);
            return "redirect:/login";
        }
        model.addAttribute("existsEmail", true);
        return "auth/register";
    }
}
