package com.urlshortener.service;

import com.urlshortener.dto.request.CreateUrlRequest;
import com.urlshortener.dto.request.UpdateUrlRequest;
import com.urlshortener.dto.response.UrlResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UrlService {
    UrlResponse createShortUrl(CreateUrlRequest request, String userId);
    String redirect(String shortCode, HttpServletRequest request);
    UrlResponse getUrlByShortCode(String shortCode, String userId);
    List<UrlResponse> getUserUrls(String userId);
    UrlResponse updateUrl(String shortCode, UpdateUrlRequest request, String userId);
    void deleteUrl(String shortCode, String userId);
}
