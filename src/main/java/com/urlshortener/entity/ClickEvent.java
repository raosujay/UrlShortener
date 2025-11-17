package com.urlshortener.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "click_events")
public class ClickEvent {

    @Id
    private String id;

    @Indexed
    private String shortCode;

    private String ipAddress;

    private String country;

    private String region;

    private String city;

    private String referrer;

    private String userAgent;

    private String utmSource;

    private String utmMedium;

    private String utmCampaign;

    @CreatedDate
    private LocalDateTime clickedAt;
}
