package com.urlshortener.service;

import java.util.Map;

public interface UserAgentService {
    Map<String, String> parseUserAgent(String userAgent);
}
