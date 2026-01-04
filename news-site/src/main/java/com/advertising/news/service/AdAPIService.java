package com.advertising.news.service;

import com.advertising.news.dto.AdResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class AdAPIService {

    @Value("${ad.service.url:http://localhost:8081}")
    private String adServiceUrl;

    private final RestTemplate restTemplate;

    public AdAPIService() {
        this.restTemplate = new RestTemplate();
    }

    public List<AdResponse> getActiveAds() {
        try {
            String url = adServiceUrl + "/api/ads/active?website=news";
            AdResponse[] response = restTemplate.getForObject(url, AdResponse[].class);
            return response != null ? Arrays.asList(response) : Arrays.asList();
        } catch (Exception e) {
            e.printStackTrace();
            return Arrays.asList();
        }
    }
}