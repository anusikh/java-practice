package com.anusikh.authservice.service;

import com.anusikh.authservice.dto.IdTokenRequest;
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

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Value("${app.googleClientId}")
    private String clientId;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = null;
        try {
            userInfo = userInfoRepository.findByName(username);
        } catch (Exception e) {
            System.out.println("exception");
        }
        return new UserInfoUserDetails(userInfo.get());
    }

    public String loginOauthGoogle(IdTokenRequest tokenRequest) {
        try {
            UserInfo user = verifyIdToken(tokenRequest.getIdToken());
            return jwtUtil.generateToken(user.getName());
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @Transactional
    private UserInfo verifyIdToken(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                    new GsonFactory())
                    .setAudience(Collections.singletonList(clientId)).build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (idToken == null) {
                return null;
            }
            Payload payload = googleIdToken.getPayload();
            String userId = payload.getSubject();
            String name = (String) payload.get("name");
            String email = (String) payload.get("email");

            // Check if the user is there, else create a new user
            Optional<UserInfo> existingUser = userInfoRepository.findByEmail(email);
            if (existingUser.isPresent() == true) {
                return existingUser.get();
            } else {
                UserInfo newUser = new UserInfo(name, email, "ROLE_USER", userId);
                userInfoRepository.save(newUser);
                return newUser;
            }

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
