package com.urlshortener.service.impl;

import com.urlshortener.dto.response.AnalyticsResponse;
import com.urlshortener.entity.ClickEvent;
import com.urlshortener.entity.ShortUrl;
import com.urlshortener.exception.ResourceNotFoundException;
import com.urlshortener.exception.UnauthorizedException;
import com.urlshortener.repository.ClickEventRepository;
import com.urlshortener.repository.ShortUrlRepository;
import com.urlshortener.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

        private final ShortUrlRepository shortUrlRepository;
        private final ClickEventRepository clickEventRepository;

        @Override
        public AnalyticsResponse getAnalytics(String shortCode, String userId) {
                ShortUrl shortUrl = shortUrlRepository.findByShortCode(shortCode)
                                .orElseThrow(() -> new ResourceNotFoundException("Short URL not found"));

                if (!shortUrl.getUserId().equals(userId)) {
                        throw new UnauthorizedException("Not authorized to view analytics for this URL");
                }

                List<ClickEvent> clicks = clickEventRepository.findByShortCode(shortCode);

                Map<String, Long> clicksByCountry = clicks.stream()
                                .filter(click -> click.getCountry() != null)
                                .collect(Collectors.groupingBy(ClickEvent::getCountry, Collectors.counting()));

                Map<String, Long> clicksByRegion = clicks.stream()
                                .filter(click -> click.getRegion() != null)
                                .collect(Collectors.groupingBy(ClickEvent::getRegion, Collectors.counting()));

                Map<String, Long> clicksByReferrer = clicks.stream()
                                .filter(click -> click.getReferrer() != null)
                                .collect(Collectors.groupingBy(ClickEvent::getReferrer, Collectors.counting()));

                Map<String, Long> clicksByDate = clicks.stream()
                                .collect(Collectors.groupingBy(
                                                click -> click.getClickedAt().toLocalDate()
                                                                .format(DateTimeFormatter.ISO_DATE),
                                                Collectors.counting()));

                Map<String, Long> clicksByDeviceType = clicks.stream()
                                .filter(click -> click.getDeviceType() != null)
                                .collect(Collectors.groupingBy(ClickEvent::getDeviceType, Collectors.counting()));

                Map<String, Long> clicksByBrowser = clicks.stream()
                                .filter(click -> click.getBrowser() != null)
                                .collect(Collectors.groupingBy(ClickEvent::getBrowser, Collectors.counting()));

                Map<String, Long> clicksByOperatingSystem = clicks.stream()
                                .filter(click -> click.getOperatingSystem() != null)
                                .collect(Collectors.groupingBy(ClickEvent::getOperatingSystem, Collectors.counting()));

                return AnalyticsResponse.builder()
                                .shortCode(shortCode)
                                .totalClicks(shortUrl.getTotalClicks())
                                .clicksByCountry(clicksByCountry)
                                .clicksByRegion(clicksByRegion)
                                .clicksByReferrer(clicksByReferrer)
                                .clicksByDate(clicksByDate)
                                .clicksByDeviceType(clicksByDeviceType)
                                .clicksByBrowser(clicksByBrowser)
                                .clicksByOperatingSystem(clicksByOperatingSystem)
                                .build();
        }
}
