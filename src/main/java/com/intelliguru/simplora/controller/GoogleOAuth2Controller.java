package com.intelliguru.simplora.controller;

import com.intelliguru.simplora.entity.User;
import com.intelliguru.simplora.repository.UserRepository;
import com.intelliguru.simplora.service.UserDetailsServiceImpl;
import com.intelliguru.simplora.service.oauth.GoogleOAuthService;
import com.intelliguru.simplora.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/auth/google")
@Slf4j
public class GoogleOAuth2Controller {

    @Autowired
    UserDetailsServiceImpl userDetailsService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoogleOAuthService googleOAuthService;

    @GetMapping("/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String code) {
        try {
            ResponseEntity<Map> userInfoResponse = googleOAuthService.getUserInfoResponse(code);
            if (userInfoResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                UserDetails userDetails = null;
                try {
                    userDetails = userDetailsService.loadUserByUsername(email);
                } catch (UsernameNotFoundException e) {
                    User user = new User();
                    user.setEmail(email);
                    user.setUserName(email);
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    user.setRoles(List.of("USER"));
                    userRepository.save(user);
                }
                String jwtToken = JwtUtil.generateToken(email);
                return ResponseEntity.ok(Collections.singletonMap("token", jwtToken));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Exception occurred while handleGoogleCallback ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}

/*

https://accounts.google.com/o/oauth2/auth?
client_id=YOUR_CLIENT_ID
    &redirect_uri=YOUR_REDIRECT_URI
    &response_type=code
    &scope=email profile
    &access_type=offline
    &prompt=consent

*/
