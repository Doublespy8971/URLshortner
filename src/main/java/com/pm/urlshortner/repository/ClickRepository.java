package com.pm.urlshortner.repository;

import com.pm.urlshortner.model.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickRepository extends JpaRepository<Click, Long> {

    long countByShortCode(String shortCode);

    List<Click> findAllByShortCodeOrderByClickedAtDesc(String shortCode);

    long countByShortCodeAndClickedAtAfter(String shortCode, LocalDateTime timestamp);
}

