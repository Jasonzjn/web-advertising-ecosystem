package com.advertising.adservice.service;

import com.advertising.adservice.dto.AdRequest;
import com.advertising.adservice.dto.AdResponse;
import com.advertising.adservice.entity.Ad;
import com.advertising.adservice.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;

    public List<AdResponse> getActiveAds(String website) {
        List<Ad> ads = adRepository.findActiveAdsByWebsite(website, LocalDateTime.now());
        return ads.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<AdResponse> getAdsForWebsite(String websiteType) {
        // 这个方法为前端提供服务，调用getActiveAds
        return getActiveAds(websiteType);
    }

    public List<AdResponse> getAllAds() {
        List<Ad> ads = adRepository.findAll();
        return ads.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public AdResponse createAd(AdRequest adRequest) {
        Ad ad = new Ad(
                adRequest.getTitle(),
                adRequest.getContent(),
                adRequest.getImageUrl(),
                adRequest.getTargetUrl(),
                adRequest.getTargetWebsite(),
                adRequest.getStartDate(),
                adRequest.getEndDate()
        );

        Ad savedAd = adRepository.save(ad);
        return convertToResponse(savedAd);
    }

    public AdResponse getAdById(Long id) {
        Ad ad = adRepository.findById(id).orElse(null);
        return ad != null ? convertToResponse(ad) : null;
    }

    public AdResponse updateAd(Long id, AdRequest adRequest) {
        Ad existingAd = adRepository.findById(id).orElse(null);
        if (existingAd == null) {
            return null;
        }

        existingAd.setTitle(adRequest.getTitle());
        existingAd.setContent(adRequest.getContent());
        existingAd.setImageUrl(adRequest.getImageUrl());
        existingAd.setTargetUrl(adRequest.getTargetUrl());
        existingAd.setTargetWebsite(adRequest.getTargetWebsite());
        existingAd.setStartDate(adRequest.getStartDate());
        existingAd.setEndDate(adRequest.getEndDate());

        Ad updatedAd = adRepository.save(existingAd);
        return convertToResponse(updatedAd);
    }

    public boolean deleteAd(Long id) {
        if (adRepository.existsById(id)) {
            adRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private AdResponse convertToResponse(Ad ad) {
        return new AdResponse(
                ad.getId(),
                ad.getTitle(),
                ad.getContent(),
                ad.getImageUrl(),
                ad.getTargetUrl(),
                ad.getTargetWebsite(),
                ad.getStartDate(),
                ad.getEndDate(),
                ad.getCreatedAt(),
                ad.getUpdatedAt()
        );
    }
}