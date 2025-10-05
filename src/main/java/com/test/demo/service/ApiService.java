package com.test.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.test.demo.model.SavedApi;
import com.test.demo.model.User;
import com.test.demo.repository.ApiRepository;
import com.test.demo.repository.UserRepository;

@Service
public class ApiService {

    @Autowired
    private ApiRepository savedApiRepository;

    @Autowired
    private UserRepository userRepository;

    // Save API for a user
    public SavedApi saveApi(String apiName, String apiKey) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                                            .getAuthentication()
                                            .getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username)
                      .orElseThrow(() -> new RuntimeException("User not found"));
                      
        SavedApi api = new SavedApi();
        api.setUser(user);
        api.setApiName(apiName);
        api.setApiKey(apiKey);

        return savedApiRepository.save(api);
    }

    public List<SavedApi> getApisByUser() {
        // Get logged-in user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                                            .getAuthentication()
                                            .getPrincipal();
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username)
                      .orElseThrow(() -> new RuntimeException("User not found"));

        return savedApiRepository.findByUser(user);
    }
}
