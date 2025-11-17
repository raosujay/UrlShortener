package com.urlshortener.service.impl;

import com.urlshortener.service.ShortCodeGeneratorService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ShortCodeGeneratorServiceImpl implements ShortCodeGeneratorService {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_CODE_LENGTH = 7;
    private final SecureRandom random = new SecureRandom();

    @Override
    public String generateShortCode() {
        StringBuilder shortCode = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            shortCode.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return shortCode.toString();
    }
}
