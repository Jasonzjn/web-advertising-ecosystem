package com.advertising.shopping.controller;

import com.advertising.shopping.service.AdAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdController {

    @Autowired
    private AdAPIService adAPIService;

    @GetMapping("/ads")
    public ResponseEntity<?> getAds() {
        return ResponseEntity.ok(adAPIService.getActiveAds());
    }
}