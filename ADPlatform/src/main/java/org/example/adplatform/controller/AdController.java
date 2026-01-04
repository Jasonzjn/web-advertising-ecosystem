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

@RestController
public class AdController {
    @Autowired
    private AdService adService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createAd(@RequestBody Map<String, Object> request,
                                      @RequestHeader("Authorization") String authHeader) {
        try {
            // 简化验证，实际应该使用JWT或Session
            String username = (String) request.get("username");
            String password = (String) request.get("password");

            User user = userService.login(username, password);

            String name = (String) request.get("name");
            String content = (String) request.get("content");
            String adTypeStr = (String) request.get("adType");

            Advertisement.AdType adType = Advertisement.AdType.valueOf(adTypeStr.toUpperCase());

            Advertisement ad = adService.createAd(name, content, adType, user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "广告创建成功");
            response.put("adId", ad.getId());
            response.put("name", ad.getName());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/my-ads")
    public ResponseEntity<?> getMyAds(@RequestParam String username,
                                      @RequestParam String password) {
        try {
            User user = userService.login(username, password);
            List<Advertisement> ads = adService.getUserAds(user);

            return ResponseEntity.ok(ads);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
