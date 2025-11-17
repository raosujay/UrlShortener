package com.urlshortener.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUrlRequest {
    private String originalUrl;
    private LocalDateTime expiresAt;
    private Boolean active;
}
