package com.anusikh.authservice.service;

import com.anusikh.authservice.dao.IdTokenRequest;
import com.anusikh.authservice.entity.UserInfo;
import com.anusikh.authservice.entity.UserInfoUserDetails;
import com.anusikh.authservice.repository.UserInfoRepository;
import com.anusikh.authservice.util.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Component
@Service
public class UserInfoUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoUserDetailsService.class);

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Value("${app.googleClientId}")
    private String clientId;

    public String register(UserInfo userInfo) {
        try {
            Optional<UserInfo> u = userInfoRepository.findByName(userInfo.getName());
            if (!u.isPresent()) {
                userInfoRepository.save(userInfo);
                return "new user created";
            } else {
                return "user already exists";
            }
        } catch (Exception e) {
            LOGGER.info("UserInfoUserDetailsService::loadUserByUsername::", e);
            return "something went wrong";
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = null;
        try {
            userInfo = userInfoRepository.findByName(username);
        } catch (Exception e) {
            LOGGER.info("UserInfoUserDetailsService::loadUserByUsername::", e);
        }
        return new UserInfoUserDetails(userInfo.get());
    }

    public String loginOauthGoogle(IdTokenRequest idTokenRequest) {
        try {
            UserInfo user = verifyIdToken(idTokenRequest.getIdToken());
            return jwtUtil.generateToken("");
        } catch (Exception e) {
            LOGGER.info("UserInfoUserDetailsService::loginOauthGoogle::", e);
            return null;
        }
    }

    @Transactional
    private UserInfo verifyIdToken(String idToken) {
        try {
            GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                    new GsonFactory()).setAudience(Collections.singletonList(clientId)).build();
            GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(idToken);
            if (idToken == null) {
                return null;
            }
            Payload payload = googleIdToken.getPayload();
            String userId = payload.getSubject();
            String name = (String) payload.get("name");
            String email = (String) payload.get("email");

            Optional<UserInfo> existingUser = userInfoRepository.findByEmail(email);
            if (existingUser.isPresent() == true) {
                return existingUser.get();
            } else {
                UserInfo newUser = new UserInfo(name, email, "ROLE_USER", userId);
                userInfoRepository.save(newUser);
                return newUser;
            }

        } catch (Exception e) {
            LOGGER.info("UserInfoUserDetailsService::verifyIdToken::", e);
            return null;
        }
    }

}
