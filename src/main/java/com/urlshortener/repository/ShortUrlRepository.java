package com.urlshortener.repository;

import com.urlshortener.entity.ShortUrl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShortUrlRepository extends MongoRepository<ShortUrl, String> {
    Optional<ShortUrl> findByShortCode(String shortCode);
    List<ShortUrl> findByUserId(String userId);
    List<ShortUrl> findByUserIdAndActiveTrue(String userId);
    boolean existsByShortCode(String shortCode);
    List<ShortUrl> findByExpiresAtBeforeAndActiveTrue(LocalDateTime dateTime);
}
