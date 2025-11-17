package com.urlshortener.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "short_urls")
public class ShortUrl {

    @Id
    private String id;

    @Indexed(unique = true)
    private String shortCode;

    private String originalUrl;

    @Indexed
    private String userId;

    private Long totalClicks;

    private LocalDateTime expiresAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private boolean active;

    public ShortUrl(String shortCode, String originalUrl, String userId, LocalDateTime expiresAt) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.userId = userId;
        this.expiresAt = expiresAt;
        this.totalClicks = 0L;
        this.active = true;
    }
}
