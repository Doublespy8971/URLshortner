package com.pm.urlshortner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ShortenResponse {

    private String shortUrl;
    private String shortCode;
    private String originalUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public ShortenResponse() {}

    public ShortenResponse(String shortUrl, String shortCode, String originalUrl, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.shortUrl = shortUrl;
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getShortUrl() { return shortUrl; }
    public void setShortUrl(String shortUrl) { this.shortUrl = shortUrl; }
    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public static ShortenResponseBuilder builder() {
        return new ShortenResponseBuilder();
    }

    public static class ShortenResponseBuilder {
        private String shortUrl;
        private String shortCode;
        private String originalUrl;
        private LocalDateTime createdAt;
        private LocalDateTime expiresAt;

        public ShortenResponseBuilder shortUrl(String shortUrl) { this.shortUrl = shortUrl; return this; }
        public ShortenResponseBuilder shortCode(String shortCode) { this.shortCode = shortCode; return this; }
        public ShortenResponseBuilder originalUrl(String originalUrl) { this.originalUrl = originalUrl; return this; }
        public ShortenResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ShortenResponseBuilder expiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; return this; }
        public ShortenResponse build() {
            return new ShortenResponse(shortUrl, shortCode, originalUrl, createdAt, expiresAt);
        }
    }
}

