package com.pm.urlshortner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AnalyticsResponse {

    private String shortCode;
    private String originalUrl;
    private Long totalClicks;
    private Long clicksToday;
    private Long clicksThisWeek;
    private LocalDateTime createdAt;

    public AnalyticsResponse() {}

    public AnalyticsResponse(String shortCode, String originalUrl, Long totalClicks, Long clicksToday, Long clicksThisWeek, LocalDateTime createdAt) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.totalClicks = totalClicks;
        this.clicksToday = clicksToday;
        this.clicksThisWeek = clicksThisWeek;
        this.createdAt = createdAt;
    }

    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public Long getTotalClicks() { return totalClicks; }
    public void setTotalClicks(Long totalClicks) { this.totalClicks = totalClicks; }
    public Long getClicksToday() { return clicksToday; }
    public void setClicksToday(Long clicksToday) { this.clicksToday = clicksToday; }
    public Long getClicksThisWeek() { return clicksThisWeek; }
    public void setClicksThisWeek(Long clicksThisWeek) { this.clicksThisWeek = clicksThisWeek; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static AnalyticsResponseBuilder builder() {
        return new AnalyticsResponseBuilder();
    }

    public static class AnalyticsResponseBuilder {
        private String shortCode;
        private String originalUrl;
        private Long totalClicks;
        private Long clicksToday;
        private Long clicksThisWeek;
        private LocalDateTime createdAt;

        public AnalyticsResponseBuilder shortCode(String shortCode) { this.shortCode = shortCode; return this; }
        public AnalyticsResponseBuilder originalUrl(String originalUrl) { this.originalUrl = originalUrl; return this; }
        public AnalyticsResponseBuilder totalClicks(Long totalClicks) { this.totalClicks = totalClicks; return this; }
        public AnalyticsResponseBuilder clicksToday(Long clicksToday) { this.clicksToday = clicksToday; return this; }
        public AnalyticsResponseBuilder clicksThisWeek(Long clicksThisWeek) { this.clicksThisWeek = clicksThisWeek; return this; }
        public AnalyticsResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public AnalyticsResponse build() {
            return new AnalyticsResponse(shortCode, originalUrl, totalClicks, clicksToday, clicksThisWeek, createdAt);
        }
    }
}

