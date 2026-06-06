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

    public Click() {}

    public Click(String shortCode, String ipAddress, String referrer) {
        this.shortCode = shortCode;
        this.ipAddress = ipAddress;
        this.referrer = referrer;
    }

    @PrePersist
    protected void onClock() {
        this.clickedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getShortCode() { return shortCode; }
    public void setShortCode(String shortCode) { this.shortCode = shortCode; }
    public LocalDateTime getClickedAt() { return clickedAt; }
    public void setClickedAt(LocalDateTime clickedAt) { this.clickedAt = clickedAt; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getReferrer() { return referrer; }
    public void setReferrer(String referrer) { this.referrer = referrer; }

    public static ClickBuilder builder() {
        return new ClickBuilder();
    }

    public static class ClickBuilder {
        private String shortCode;
        private String ipAddress;
        private String referrer;

        public ClickBuilder shortCode(String shortCode) { this.shortCode = shortCode; return this; }
        public ClickBuilder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public ClickBuilder referrer(String referrer) { this.referrer = referrer; return this; }
        public Click build() {
            return new Click(shortCode, ipAddress, referrer);
        }
    }
}

