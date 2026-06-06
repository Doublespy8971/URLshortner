package com.pm.urlshortner.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

public class ShortenRequest {

    @NotBlank(message = "Original URL is required")
    @URL(message = "Invalid URL format")
    private String originalUrl;

    private String customCode;

    @Min(value = 1, message = "Expiries must be at least 1 day")
    @Max(value = 365, message = "Expiries cannot exceed 365 days")
    private Integer expiresInDays;

    public ShortenRequest() {}

    public ShortenRequest(String originalUrl, String customCode, Integer expiresInDays) {
        this.originalUrl = originalUrl;
        this.customCode = customCode;
        this.expiresInDays = expiresInDays;
    }

    public String getOriginalUrl() { return originalUrl; }
    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }
    public String getCustomCode() { return customCode; }
    public void setCustomCode(String customCode) { this.customCode = customCode; }
    public Integer getExpiresInDays() { return expiresInDays; }
    public void setExpiresInDays(Integer expiresInDays) { this.expiresInDays = expiresInDays; }
}

