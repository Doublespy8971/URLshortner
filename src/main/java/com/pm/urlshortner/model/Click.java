package com.pm.urlshortner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "clicks", indexes = {
    @Index(name = "idx_click_short_code", columnList = "shortCode")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Click {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String shortCode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime clickedAt;

    private String ipAddress;

    private String referrer;

    @PrePersist
    protected void onClock() {
        this.clickedAt = LocalDateTime.now();
    }
}

