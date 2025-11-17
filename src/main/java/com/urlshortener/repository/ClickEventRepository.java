package com.urlshortener.repository;

import com.urlshortener.entity.ClickEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends MongoRepository<ClickEvent, String> {
    List<ClickEvent> findByShortCode(String shortCode);
    List<ClickEvent> findByShortCodeAndClickedAtBetween(String shortCode, LocalDateTime start, LocalDateTime end);
    Long countByShortCode(String shortCode);

    @Query("{ 'shortCode': ?0 }")
    List<ClickEvent> findClicksByShortCode(String shortCode);
}
