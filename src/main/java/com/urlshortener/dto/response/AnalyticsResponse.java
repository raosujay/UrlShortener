package com.urlshortener.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "a1b2c3d4")
    private String shortCode;

    @Schema(example = "150")
    private Long totalClicks;

    @Schema(example = "{\"United States\": 100, \"India\": 50}")
    private Map<String, Long> clicksByCountry;

    @Schema(example = "{\"California\": 80, \"Karnataka\": 40}")
    private Map<String, Long> clicksByRegion;

    @Schema(example = "{\"google.com\": 90, \"direct\": 60}")
    private Map<String, Long> clicksByReferrer;

    @Schema(example = "{\"2023-10-27\": 50, \"2023-10-28\": 100}")
    private Map<String, Long> clicksByDate;

    @Schema(example = "{\"Mobile\": 80, \"Desktop\": 70}")
    private Map<String, Long> clicksByDeviceType;

    @Schema(example = "{\"Chrome\": 100, \"Firefox\": 50}")
    private Map<String, Long> clicksByBrowser;

    @Schema(example = "{\"Windows\": 90, \"Android\": 60}")
    private Map<String, Long> clicksByOperatingSystem;
}
