package com.test.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.test.demo.model.User;
import com.test.demo.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Convert comma-separated roles string into a list of GrantedAuthority
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String role : user.getRoles().split(",")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.trim()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                authorities);
    }

}