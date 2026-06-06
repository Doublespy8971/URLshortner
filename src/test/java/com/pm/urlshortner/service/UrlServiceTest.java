package com.pm.urlshortner.service;

import com.pm.urlshortner.dto.ShortenRequest;
import com.pm.urlshortner.dto.ShortenResponse;
import com.pm.urlshortner.exception.InvalidCustomCodeException;
import com.pm.urlshortner.exception.UrlExpiredException;
import com.pm.urlshortner.exception.UrlNotFoundException;
import com.pm.urlshortner.model.Url;
import com.pm.urlshortner.repository.ClickRepository;
import com.pm.urlshortner.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    private UrlService urlService;

    @Mock
    private UrlRepository urlRepository;
    @Mock
    private ClickRepository clickRepository;
    @Mock
    private RedisService redisService;
    @Mock
    private ShortCodeGenerator shortCodeGenerator;

    @BeforeEach
    void setUp() {
        urlService = new UrlService(urlRepository, clickRepository, redisService, shortCodeGenerator);
        ReflectionTestUtils.setField(urlService, "baseUrl", "http://localhost:8080/");
    }

    @Test
    void shorten_WithValidUrl_ShouldReturnResponse() {
        ShortenRequest request = new ShortenRequest("https://example.com", null, null);
        String code = "abcdefg";
        Url url = new Url(code, "https://example.com", null, 0L);
        url.setCreatedAt(LocalDateTime.now());

        when(shortCodeGenerator.generateUnique(urlRepository)).thenReturn(code);
        when(urlRepository.save(any(Url.class))).thenReturn(url);

        ShortenResponse response = urlService.shorten(request);

        assertNotNull(response);
        assertEquals(code, response.getShortCode());
        assertEquals("https://example.com", response.getOriginalUrl());
        verify(redisService).set(eq(code), eq("https://example.com"));
    }

    @Test
    void shorten_WithExistingCustomCode_ShouldThrowException() {
        ShortenRequest request = new ShortenRequest("https://example.com", "custom", null);

        when(shortCodeGenerator.isValidCustomCode("custom")).thenReturn(true);
        when(urlRepository.existsByShortCode("custom")).thenReturn(true);

        assertThrows(InvalidCustomCodeException.class, () -> urlService.shorten(request));
    }

    @Test
    void getOriginalUrl_InRedis_ShouldReturnFromCache() {
        String code = "abcdefg";
        String originalUrl = "https://example.com";
        when(redisService.get(code)).thenReturn(originalUrl);

        String result = urlService.getOriginalUrl(code);

        assertEquals(originalUrl, result);
        verifyNoInteractions(urlRepository);
    }

    @Test
    void getOriginalUrl_NotInRedis_ShouldFallbackToPostgres() {
        String code = "abcdefg";
        String originalUrl = "https://example.com";
        Url url = new Url(code, originalUrl, null, 0L);

        when(redisService.get(code)).thenReturn(null);
        when(urlRepository.findByShortCode(code)).thenReturn(Optional.of(url));

        String result = urlService.getOriginalUrl(code);

        assertEquals(originalUrl, result);
        verify(redisService).set(eq(code), eq(originalUrl));
    }

    @Test
    void getOriginalUrl_UnknownCode_ShouldThrowNotFound() {
        String code = "unknown";
        when(redisService.get(code)).thenReturn(null);
        when(urlRepository.findByShortCode(code)).thenReturn(Optional.empty());

        assertThrows(UrlNotFoundException.class, () -> urlService.getOriginalUrl(code));
    }

    @Test
    void getOriginalUrl_Expired_ShouldThrowExpiredAndCleanup() {
        String code = "expired";
        Url url = new Url(code, "https://example.com", LocalDateTime.now().minusDays(1), 0L);

        when(redisService.get(code)).thenReturn(null);
        when(urlRepository.findByShortCode(code)).thenReturn(Optional.of(url));

        assertThrows(UrlExpiredException.class, () -> urlService.getOriginalUrl(code));
        verify(urlRepository).delete(url);
        verify(redisService).delete(code);
    }
}

