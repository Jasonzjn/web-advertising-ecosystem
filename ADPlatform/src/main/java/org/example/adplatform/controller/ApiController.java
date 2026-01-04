package org.example.adplatform.controller;

import org.example.adplatform.model.Advertisement;
import org.example.adplatform.model.User;
import org.example.adplatform.service.AdService;
import org.example.adplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/public")
public class ApiController {

    @Autowired
    private AdService adService;

    @GetMapping("/ad/random")
    public ResponseEntity<?> getRandomAd(@RequestParam(required = false) String type) {
        try {
            List<Advertisement> ads;

            if (type != null && !type.isEmpty()) {
                Advertisement.AdType adType = Advertisement.AdType.valueOf(type.toUpperCase());
                ads = adService.getActiveAds().stream()
                        .filter(ad -> ad.getAdType() == adType)
                        .toList();
            } else {
                ads = adService.getActiveAds();
            }

            if (ads.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // 随机选择一个广告
            Random random = new Random();
            Advertisement ad = ads.get(random.nextInt(ads.size()));

            // 增加展示次数
            adService.incrementImpressions(ad.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("id", ad.getId());
            response.put("name", ad.getName());
            response.put("content", ad.getContent());
            response.put("type", ad.getAdType().toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/ad/{id}/click")
    public ResponseEntity<?> recordClick(@PathVariable Long id) {
        try {
            adService.incrementClicks(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "点击记录成功");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/ad/{id}")
    public ResponseEntity<?> getAdById(@PathVariable Long id) {
        try {
            Advertisement ad = adService.getAdById(id);

            // 增加展示次数
            adService.incrementImpressions(id);

            Map<String, Object> response = new HashMap<>();
            response.put("id", ad.getId());
            response.put("name", ad.getName());
            response.put("content", ad.getContent());
            response.put("type", ad.getAdType().toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
