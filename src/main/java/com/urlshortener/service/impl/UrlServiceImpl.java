package com.urlshortener.service.impl;

import com.urlshortener.dto.request.CreateUrlRequest;
import com.urlshortener.dto.request.UpdateUrlRequest;
import com.urlshortener.dto.response.UrlResponse;
import com.urlshortener.entity.ClickEvent;
import com.urlshortener.entity.ShortUrl;
import com.urlshortener.exception.DuplicateResourceException;
import com.urlshortener.exception.ResourceNotFoundException;
import com.urlshortener.exception.UnauthorizedException;
import com.urlshortener.repository.ClickEventRepository;
import com.urlshortener.repository.ShortUrlRepository;
import com.urlshortener.service.ShortCodeGeneratorService;
import com.urlshortener.service.UrlService;
import com.urlshortener.util.GeoLocationUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final ClickEventRepository clickEventRepository;
    private final ShortCodeGeneratorService shortCodeGeneratorService;
    private final GeoLocationUtil geoLocationUtil;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    @Transactional
    public UrlResponse createShortUrl(CreateUrlRequest request, String userId) {
        String shortCode;

        if (request.getCustomAlias() != null && !request.getCustomAlias().isEmpty()) {
            if (shortUrlRepository.existsByShortCode(request.getCustomAlias())) {
                throw new DuplicateResourceException("Custom alias already exists");
            }
            shortCode = request.getCustomAlias();
        } else {
            shortCode = generateUniqueShortCode();
        }

        ShortUrl shortUrl = ShortUrl.builder()
                .shortCode(shortCode)
                .originalUrl(request.getOriginalUrl())
                .userId(userId)
                .expiresAt(request.getExpiresAt())
                .totalClicks(0L)
                .active(true)
                .build();

        shortUrlRepository.save(shortUrl);

        return mapToResponse(shortUrl);
    }

    @Override
    @Transactional
    public String redirect(String shortCode, HttpServletRequest request) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

        if (!shortUrl.isActive()) {
            throw new ResourceNotFoundException("Short URL is inactive");
        }

        if (shortUrl.getExpiresAt() != null && shortUrl.getExpiresAt().isBefore(LocalDateTime.now())) {
            shortUrl.setActive(false);
            shortUrlRepository.save(shortUrl);
            throw new ResourceNotFoundException("Short URL has expired");
        }

        trackClick(shortUrl, request);

        shortUrl.setTotalClicks(shortUrl.getTotalClicks() + 1);
        shortUrlRepository.save(shortUrl);

        return shortUrl.getOriginalUrl();
    }

    @Override
    public UrlResponse getUrlByShortCode(String shortCode, String userId) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

        if (!shortUrl.getUserId().equals(userId)) {
            throw new UnauthorizedException("Not authorized to access this URL");
        }

        return mapToResponse(shortUrl);
    }

    @Override
    public List<UrlResponse> getUserUrls(String userId) {
        List<ShortUrl> urls = shortUrlRepository.findByUserId(userId);
        return urls.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UrlResponse updateUrl(String shortCode, UpdateUrlRequest request, String userId) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

        if (!shortUrl.getUserId().equals(userId)) {
            throw new UnauthorizedException("Not authorized to update this URL");
        }

        if (request.getOriginalUrl() != null) {
            shortUrl.setOriginalUrl(request.getOriginalUrl());
        }
        if (request.getExpiresAt() != null) {
            shortUrl.setExpiresAt(request.getExpiresAt());
        }
        if (request.getActive() != null) {
            shortUrl.setActive(request.getActive());
        }

        shortUrlRepository.save(shortUrl);

        return mapToResponse(shortUrl);
    }

    @Override
    @Transactional
    public void deleteUrl(String shortCode, String userId) {
        ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

        if (!shortUrl.getUserId().equals(userId)) {
            throw new UnauthorizedException("Not authorized to delete this URL");
        }

        shortUrlRepository.delete(shortUrl);
    }

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = shortCodeGeneratorService.generateShortCode();
        } while (shortUrlRepository.existsByShortCode(shortCode));
        return shortCode;
    }

    private void trackClick(ShortUrl shortUrl, HttpServletRequest request) {
        String ipAddress = getClientIp(request);
        String referrer = getReferrer(request);
        String userAgent = request.getHeader("User-Agent");

        Map<String, String> geoData = geoLocationUtil.getGeoLocation(ipAddress);

        ClickEvent clickEvent = ClickEvent.builder()
                .shortCode(shortUrl.getShortCode())
                .ipAddress(ipAddress)
                .country(geoData.get("country"))
                .region(geoData.get("region"))
                .city(geoData.get("city"))
                .referrer(referrer)
                .userAgent(userAgent)
                .utmSource(request.getParameter("utm_source"))
                .utmMedium(request.getParameter("utm_medium"))
                .utmCampaign(request.getParameter("utm_campaign"))
                .build();

        clickEventRepository.save(clickEvent);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String getReferrer(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        if (referrer == null || referrer.isEmpty()) {
            return "direct";
        }
        if (referrer.contains("facebook") || referrer.contains("twitter") ||
                referrer.contains("linkedin") || referrer.contains("instagram")) {
            return "social";
        }
        if (referrer.contains("google") || referrer.contains("bing") || referrer.contains("yahoo")) {
            return "search";
        }
        return referrer;
    }

    private UrlResponse mapToResponse(ShortUrl shortUrl) {
        return UrlResponse.builder()
                .id(shortUrl.getId())
                .shortCode(shortUrl.getShortCode())
                .shortUrl(baseUrl + "/r/" + shortUrl.getShortCode())
                .originalUrl(shortUrl.getOriginalUrl())
                .totalClicks(shortUrl.getTotalClicks())
                .expiresAt(shortUrl.getExpiresAt())
                .createdAt(shortUrl.getCreatedAt())
                .active(shortUrl.isActive())
                .build();
    }
}
