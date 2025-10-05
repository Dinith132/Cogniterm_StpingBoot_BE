package com.test.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.demo.model.UserHistory;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, UUID> {

    // Fetch all histories for a user, most recent first
    List<UserHistory> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
