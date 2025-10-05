package com.test.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.demo.model.SavedApi;
import com.test.demo.model.User;

@Repository
public interface ApiRepository extends JpaRepository<SavedApi, UUID> {
    List<SavedApi> findByUserId(UUID userId);

    List<SavedApi> findByUser(User user);
}
