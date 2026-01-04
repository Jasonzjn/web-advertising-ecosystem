package com.advertising.adservice.repository;

import com.advertising.adservice.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    @Query("SELECT a FROM Ad a WHERE a.targetWebsite = :website OR a.targetWebsite = 'both' " +
            "AND a.startDate <= :currentTime AND a.endDate >= :currentTime " +
            "ORDER BY a.id DESC")
    List<Ad> findActiveAdsByWebsite(@Param("website") String website, @Param("currentTime") LocalDateTime currentTime);

    List<Ad> findByTargetWebsite(String targetWebsite);

    @Query("SELECT a FROM Ad a WHERE a.startDate <= :currentTime AND a.endDate >= :currentTime")
    List<Ad> findCurrentlyActiveAds(@Param("currentTime") LocalDateTime currentTime);
}