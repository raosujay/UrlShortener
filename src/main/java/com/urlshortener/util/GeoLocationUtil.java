package com.urlshortener.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GeoLocationUtil {

    private final RestTemplate restTemplate;

    public GeoLocationUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, String> getGeoLocation(String ipAddress) {
        Map<String, String> geoData = new HashMap<>();

        try {
            if (ipAddress == null || ipAddress.equals("127.0.0.1") || ipAddress.startsWith("192.168")
                    || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                geoData.put("country", "Local");
                geoData.put("region", "Local");
                geoData.put("city", "Local");
                return geoData;
            }

            // Use ip-api.com for geolocation
            String url = "http://ip-api.com/json/" + ipAddress;
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && "success".equals(response.get("status"))) {
                geoData.put("country", (String) response.getOrDefault("country", "Unknown"));
                geoData.put("region", (String) response.getOrDefault("regionName", "Unknown"));
                geoData.put("city", (String) response.getOrDefault("city", "Unknown"));
            } else {
                geoData.put("country", "Unknown");
                geoData.put("region", "Unknown");
                geoData.put("city", "Unknown");
            }

        } catch (Exception e) {
            log.error("Error getting geolocation for IP: {}", ipAddress, e);
            geoData.put("country", "Unknown");
            geoData.put("region", "Unknown");
            geoData.put("city", "Unknown");
        }

        return geoData;
    }
}
