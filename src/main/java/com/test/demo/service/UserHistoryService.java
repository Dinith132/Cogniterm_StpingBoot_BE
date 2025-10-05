package com.test.demo.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.test.demo.model.User;
import com.test.demo.model.UserHistory;
import com.test.demo.repository.UserHistoryRepository;
import com.test.demo.repository.UserRepository;

@Service
public class UserHistoryService {

    private final UserHistoryRepository historyRepository;
    private final UserRepository userRepository;

    public UserHistoryService(UserHistoryRepository historyRepository,
                              UserRepository userRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    // Save history
    public void saveHistory(String queryText, String responseText) {
        // Get logged-in user
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                      .orElseThrow(() -> new RuntimeException("User not found"));

        // Create and save history
        UserHistory history = new UserHistory();
        history.setUser(user);
        history.setQueryText(queryText);
        history.setResponseText(responseText);

        historyRepository.save(history);
    }

    // Get histories for current user
    public List<UserHistory> getUserHistories() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getPrincipal();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                      .orElseThrow(() -> new RuntimeException("User not found"));        
        return historyRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }
}
