package com.test.demo.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "saved_apis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavedApi {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(nullable = false)
    private String apiName;

    @Column(nullable = false)
    private String apiKey;

    private LocalDateTime createdAt = LocalDateTime.now();


    public String toString() {
        return "UUID"+this.id+"==api"+this.apiName+"==key"+this.apiKey;
    }
    // Getters and setters
    // ...
}
