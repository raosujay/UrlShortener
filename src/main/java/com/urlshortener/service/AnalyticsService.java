package com.urlshortener.service;

import com.urlshortener.dto.response.AnalyticsResponse;

public interface AnalyticsService {
    AnalyticsResponse getAnalytics(String shortCode, String userId);
}
