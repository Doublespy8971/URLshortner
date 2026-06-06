package com.pm.urlshortner.controller;

import com.pm.urlshortner.dto.AnalyticsResponse;
import com.pm.urlshortner.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final UrlService urlService;

    public AnalyticsController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{code}")
    public ResponseEntity<AnalyticsResponse> getAnalytics(@PathVariable String code) {
        return ResponseEntity.ok(urlService.getAnalytics(code));
    }
}

