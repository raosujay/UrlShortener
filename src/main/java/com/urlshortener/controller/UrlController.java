package com.urlshortener.controller;

import com.urlshortener.dto.request.CreateUrlRequest;
import com.urlshortener.dto.request.UpdateUrlRequest;
import com.urlshortener.dto.response.UrlResponse;
import com.urlshortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "URL Management", description = "Endpoints for managing short URLs")
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/api/v1/urls")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new short URL")
    public ResponseEntity<UrlResponse> createShortUrl(
            @Valid @RequestBody CreateUrlRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UrlResponse response = urlService.createShortUrl(request, userDetails.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/r/{shortCode}")
    @Operation(summary = "Redirect to original URL")
    public void redirect(
            @PathVariable String shortCode,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String originalUrl = urlService.redirect(shortCode, request);
        response.sendRedirect(originalUrl);
    }

    @GetMapping("/api/v1/urls/{shortCode}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get URL details by short code")
    public ResponseEntity<UrlResponse> getUrl(
            @PathVariable String shortCode,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UrlResponse response = urlService.getUrlByShortCode(shortCode, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/urls")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Get all URLs for authenticated user")
    public ResponseEntity<List<UrlResponse>> getUserUrls(@AuthenticationPrincipal UserDetails userDetails) {
        List<UrlResponse> urls = urlService.getUserUrls(userDetails.getUsername());
        return ResponseEntity.ok(urls);
    }

    @PutMapping("/api/v1/urls/{shortCode}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update URL details")
    public ResponseEntity<UrlResponse> updateUrl(
            @PathVariable String shortCode,
            @Valid @RequestBody UpdateUrlRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UrlResponse response = urlService.updateUrl(shortCode, request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/v1/urls/{shortCode}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete short URL")
    public ResponseEntity<Void> deleteUrl(
            @PathVariable String shortCode,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        urlService.deleteUrl(shortCode, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
