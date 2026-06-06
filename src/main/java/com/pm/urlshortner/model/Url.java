package com.pm.urlshortner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "urls", indexes = {
    @Index(name = "idx_url_short_code", columnList = "shortCode", unique = true)
})
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shortCode;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String originalUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Long clickCount = 0L;

    public Url() {}

    public Url(String shortCode, String originalUrl, LocalDateTime expiresAt, Long clickCount) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.expiresAt = expiresAt;
        this.clickCount = clickCount;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.clickCount == null) {
            this.clickCount = 0L;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public Long getClickCount() { return clickCount; }
    public void setClickCount(Long clickCount) { this.clickCount = clickCount; }

    public static UrlBuilder builder() {
        return new UrlBuilder();
    }

    public static class UrlBuilder {
        private String shortCode;
        private String originalUrl;
        private LocalDateTime expiresAt;
        private Long clickCount;

        public UrlBuilder shortCode(String shortCode) { this.shortCode = shortCode; return this; }
        public UrlBuilder originalUrl(String originalUrl) { this.originalUrl = originalUrl; return this; }
        public UrlBuilder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public UrlBuilder clickCount(Long clickCount) { this.clickCount = clickCount; return this; }
        public Url build() {
            return new Url(shortCode, originalUrl, expiresAt, clickCount);
        }
    }
}

