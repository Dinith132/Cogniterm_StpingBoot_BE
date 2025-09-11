package com.test.demo.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.test.demo.model.User;
import com.test.demo.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String username, String rawPassword) {
        CharSequence passwordCS = rawPassword;
        String hash = passwordEncoder.encode(passwordCS);
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hash);
        return userRepository.save(user);
    }

    public boolean checkPassword(String rawPassword, String storedHash) {
        CharSequence passwordCS = rawPassword;
        return passwordEncoder.matches(passwordCS, storedHash);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}