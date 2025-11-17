package com.urlshortener.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponse {
    private String id;
    private String shortCode;
    private String shortUrl;
    private String originalUrl;
    private Long totalClicks;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private boolean active;
}
