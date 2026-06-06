package com.pm.urlshortner.service;

import com.pm.urlshortner.dto.AnalyticsResponse;
import com.pm.urlshortner.dto.ShortenRequest;
import com.pm.urlshortner.dto.ShortenResponse;
import com.pm.urlshortner.exception.InvalidCustomCodeException;
import com.pm.urlshortner.exception.UrlNotFoundException;
import com.pm.urlshortner.model.Click;
import com.pm.urlshortner.model.Url;
import com.pm.urlshortner.repository.ClickRepository;
import com.pm.urlshortner.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UrlService {

    private static final Logger log = LoggerFactory.getLogger(UrlService.class);

    private final UrlRepository urlRepository;
    private final ClickRepository clickRepository;
    private final RedisService redisService;
    private final ShortCodeGenerator shortCodeGenerator;

    public UrlService(UrlRepository urlRepository,
                      ClickRepository clickRepository,
                      RedisService redisService,
                      ShortCodeGenerator shortCodeGenerator) {
        this.urlRepository = urlRepository;
        this.clickRepository = clickRepository;
        this.redisService = redisService;
        this.shortCodeGenerator = shortCodeGenerator;
    }

    @Value("${app.base-url:http://localhost:8080/}")
    private String baseUrl;

    @Transactional
    public ShortenResponse shorten(ShortenRequest request) {
        String shortCode;

        if (request.getCustomCode() != null && !request.getCustomCode().isEmpty()) {
            if (!shortCodeGenerator.isValidCustomCode(request.getCustomCode())) {
                throw new InvalidCustomCodeException("Invalid custom code format");
            }
            if (urlRepository.existsByShortCode(request.getCustomCode())) {
                throw new InvalidCustomCodeException("Custom code already in use");
            }
            shortCode = request.getCustomCode();
        } else {
            shortCode = shortCodeGenerator.generateUnique(urlRepository);
        }

        LocalDateTime expiresAt = null;
        if (request.getExpiresInDays() != null) {
            expiresAt = LocalDateTime.now().plusDays(request.getExpiresInDays());
        }

        Url url = Url.builder()
                .shortCode(shortCode)
                .originalUrl(request.getOriginalUrl())
                .expiresAt(expiresAt)
                .clickCount(0L)
                .build();

        url = urlRepository.save(url);

        // Cache in Redis
        if (expiresAt != null) {
            Duration ttl = Duration.between(LocalDateTime.now(), expiresAt);
            if (ttl.isPositive()) {
                redisService.set(shortCode, url.getOriginalUrl(), ttl);
            }
        } else {
            redisService.set(shortCode, url.getOriginalUrl());
        }

        return ShortenResponse.builder()
                .shortUrl(baseUrl + shortCode)
                .shortCode(shortCode)
                .originalUrl(url.getOriginalUrl())
                .createdAt(url.getCreatedAt())
                .expiresAt(url.getExpiresAt())
                .build();
    }

    public String getOriginalUrl(String code) {
        // 1. Check Redis
        String cachedUrl = redisService.get(code);
        if (cachedUrl != null) {
            return cachedUrl;
        }

        // 2. Check Database
        Url url = urlRepository.findByShortCode(code)
                .orElseThrow(UrlNotFoundException::new);

        // 3. Check Expiry
        if (url.getExpiresAt() != null && url.getExpiresAt().isBefore(LocalDateTime.now())) {
            urlRepository.delete(url);
            redisService.delete(code);
            throw new UrlNotFoundException();
        }

        // 4. Cache in Redis and return
        if (url.getExpiresAt() != null) {
            Duration ttl = Duration.between(LocalDateTime.now(), url.getExpiresAt());
            if (ttl.isPositive()) {
                redisService.set(code, url.getOriginalUrl(), ttl);
            }
        } else {
            redisService.set(code, url.getOriginalUrl());
        }

        return url.getOriginalUrl();
    }

    @Async
    @Transactional
    public void logClick(String code, String ipAddress, String referrer) {
        try {
            Click click = Click.builder()
                    .shortCode(code)
                    .ipAddress(ipAddress)
                    .referrer(referrer)
                    .build();
            clickRepository.save(click);

            urlRepository.incrementClickCount(code);
        } catch (Exception e) {
            log.error("Failed to log click for code {}: {}", code, e.getMessage());
        }
    }

    public AnalyticsResponse getAnalytics(String code) {
        Url url = urlRepository.findByShortCode(code)
                .orElseThrow(UrlNotFoundException::new);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime beginningOfToday = now.toLocalDate().atStartOfDay();
        LocalDateTime oneWeekAgo = now.minusWeeks(1);

        long totalClicks = clickRepository.countByShortCode(code);
        long clicksToday = clickRepository.countByShortCodeAndClickedAtAfter(code, beginningOfToday);
        long clicksThisWeek = clickRepository.countByShortCodeAndClickedAtAfter(code, oneWeekAgo);

        return AnalyticsResponse.builder()
                .shortCode(url.getShortCode())
                .originalUrl(url.getOriginalUrl())
                .totalClicks(totalClicks)
                .clicksToday(clicksToday)
                .clicksThisWeek(clicksThisWeek)
                .createdAt(url.getCreatedAt())
                .build();
    }
}


