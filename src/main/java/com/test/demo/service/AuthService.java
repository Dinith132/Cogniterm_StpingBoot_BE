package com.test.demo.service;

import org.springframework.stereotype.Service;

import com.test.demo.model.User;
import com.test.demo.util.JwtUtil;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;


    public AuthService(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    public String login(String username, String password) {
        User user = userService.getAllUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (userService.checkPassword(password, user.getPasswordHash())) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Invalid password");
        }
    }
}
