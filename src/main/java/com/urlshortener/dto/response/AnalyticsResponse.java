package com.urlshortener.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private String shortCode;
    private Long totalClicks;
    private Map<String, Long> clicksByCountry;
    private Map<String, Long> clicksByRegion;
    private Map<String, Long> clicksByReferrer;
    private Map<String, Long> clicksByDate;
}
