package com.advertising.adservice.controller;

import com.advertising.adservice.dto.AdRequest;
import com.advertising.adservice.dto.AdResponse;
import com.advertising.adservice.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/ads")
@CrossOrigin(origins = "*")
public class AdController {
    @Autowired
    private AdService adService;

    @GetMapping("/active")
    public ResponseEntity<List<AdResponse>> getActiveAds(@RequestParam(required = false) String website) {
        List<AdResponse> ads;
        if (website != null && !website.isEmpty()) {
            ads = adService.getActiveAds(website);
        } else {
            ads = adService.getAllAds();
        }
        return ResponseEntity.ok(ads);
    }

    @GetMapping("/for-website")
    public ResponseEntity<List<AdResponse>> getAdsForWebsite(@RequestParam String websiteType) {
        List<AdResponse> ads = adService.getAdsForWebsite(websiteType);
        return ResponseEntity.ok(ads);
    }

    @PostMapping("/create")
    public ResponseEntity<AdResponse> createAd(@Valid @RequestBody AdRequest adRequest) {
        AdResponse ad = adService.createAd(adRequest);
        return ResponseEntity.ok(ad);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdResponse>> getAllAds() {
        List<AdResponse> ads = adService.getAllAds();
        return ResponseEntity.ok(ads);
    }
}