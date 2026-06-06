package com.pm.urlshortner.controller;

import com.pm.urlshortner.dto.ShortenRequest;
import com.pm.urlshortner.dto.ShortenResponse;
import com.pm.urlshortner.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<ShortenResponse> shorten(@Valid @RequestBody ShortenRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(urlService.shorten(request));
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code, HttpServletRequest request) {
        String originalUrl = urlService.getOriginalUrl(code);

        String ipAddress = request.getRemoteAddr();
        String referrer = request.getHeader("Referer");

        urlService.logClick(code, ipAddress, referrer);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @GetMapping("/api/urls/{code}")
    public ResponseEntity<ShortenResponse> getUrlInfo(@PathVariable String code) {
        return ResponseEntity.ok(urlService.getUrlInfo(code));
    }

    @DeleteMapping("/api/urls/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        urlService.delete(code);
        return ResponseEntity.noContent().build();
    }
}

