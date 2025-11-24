package com.urlshortener.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeoLocationUtilTest {

    @Mock
    private RestTemplate restTemplate;

    private GeoLocationUtil geoLocationUtil;

    @BeforeEach
    void setUp() {
        geoLocationUtil = new GeoLocationUtil(restTemplate);
    }

    @Test
    void getGeoLocation_LocalIp_ReturnsLocal() {
        Map<String, String> result = geoLocationUtil.getGeoLocation("127.0.0.1");
        assertEquals("Local", result.get("country"));
        assertEquals("Local", result.get("region"));
        assertEquals("Local", result.get("city"));
    }

    @Test
    void getGeoLocation_ValidIp_ReturnsLocation() {
        Map<String, Object> apiResponse = new HashMap<>();
        apiResponse.put("status", "success");
        apiResponse.put("country", "United States");
        apiResponse.put("regionName", "Virginia");
        apiResponse.put("city", "Ashburn");

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(apiResponse);

        Map<String, String> result = geoLocationUtil.getGeoLocation("8.8.8.8");
        assertEquals("United States", result.get("country"));
        assertEquals("Virginia", result.get("region"));
        assertEquals("Ashburn", result.get("city"));
    }

    @Test
    void getGeoLocation_ApiError_ReturnsUnknown() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenThrow(new RuntimeException("API Error"));

        Map<String, String> result = geoLocationUtil.getGeoLocation("8.8.8.8");
        assertEquals("Unknown", result.get("country"));
        assertEquals("Unknown", result.get("region"));
        assertEquals("Unknown", result.get("city"));
    }
}
