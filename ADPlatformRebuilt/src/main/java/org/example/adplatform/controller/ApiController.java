package org.example.adplatform.controller;

import org.example.adplatform.model.Advertisement;
import org.example.adplatform.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AdService adService;

    // 获取所有广告的基本信息（不包含二进制数据）
    @GetMapping("/ads")
    public ResponseEntity<List<Map<String, Object>>> getAllAds() {
        List<Advertisement> ads = adService.getAllAds();
        List<Map<String, Object>> response = ads.stream()
                .map(ad -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", ad.getId());
                    map.put("name", ad.getName());
                    map.put("category", ad.getCategory());
                    map.put("fileName", ad.getFileName());
                    map.put("fileType", ad.getFileType());
                    map.put("fileSize", ad.getFileSize());
                    map.put("createdAt", ad.getCreatedAt());
                    return map;
                })
                .toList();

        return ResponseEntity.ok(response);
    }

    // 按类别获取广告
    @GetMapping("/ads/{category}")
    public List<Advertisement> getAdsByCategory(@PathVariable Advertisement.Category category) {
        return adService.getAdsByCategory(category);
    }

    // 获取单个广告信息（不包含二进制数据）
    @GetMapping("/ad/{id}")
    public ResponseEntity<?> getAdInfo(@PathVariable Long id) {
        Advertisement ad = adService.getAdById(id);
        if (ad == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", ad.getId());
        response.put("name", ad.getName());
        response.put("category", ad.getCategory());
        response.put("fileName", ad.getFileName());
        response.put("fileType", ad.getFileType());
        response.put("fileSize", ad.getFileSize());
        response.put("createdAt", ad.getCreatedAt());

        return ResponseEntity.ok(response);
    }

    // 核心API：直接返回广告的二进制文件数据
    @GetMapping("/ad/{id}/file")
    public ResponseEntity<byte[]> getAdFile(@PathVariable Long id) {
        Advertisement ad = adService.getAdById(id);
        if (ad == null || ad.getFileData() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ad.getFileType()));
        headers.setContentLength(ad.getFileData().length);
        headers.set("Content-Disposition", "inline; filename=\"" + ad.getFileName() + "\"");

        return new ResponseEntity<>(ad.getFileData(), headers, HttpStatus.OK);
    }

    // 核心API：返回广告的二进制数据作为Base64字符串
    @GetMapping("/ad/{id}/base64")
    public ResponseEntity<?> getAdBase64(@PathVariable Long id) {
        Advertisement ad = adService.getAdById(id);
        if (ad == null || ad.getFileData() == null) {
            return ResponseEntity.notFound().build();
        }

        String base64Data = Base64.getEncoder().encodeToString(ad.getFileData());

        Map<String, Object> response = new HashMap<>();
        response.put("id", ad.getId());
        response.put("name", ad.getName());
        response.put("category", ad.getCategory());
        response.put("fileName", ad.getFileName());
        response.put("fileType", ad.getFileType());
        response.put("fileSize", ad.getFileSize());
        response.put("base64Data", base64Data);
        response.put("dataUri", "data:" + ad.getFileType() + ";base64," + base64Data);

        return ResponseEntity.ok(response);
    }

    // 新API：直接返回Data URI格式
    @GetMapping("/ad/{id}/datauri")
    public ResponseEntity<?> getAdDataUri(@PathVariable Long id) {
        String dataUri = adService.getAdDataUri(id);
        if (dataUri == null || dataUri.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> response = new HashMap<>();
        response.put("dataUri", dataUri);

        return ResponseEntity.ok(response);
    }

    // 获取图片广告（直接返回二进制数据）
    @GetMapping("/ad/{id}/image")
    public ResponseEntity<byte[]> getAdImage(@PathVariable Long id) {
        return getAdFile(id);
    }

    // 获取视频广告（直接返回二进制数据）
    @GetMapping("/ad/{id}/video")
    public ResponseEntity<byte[]> getAdVideo(@PathVariable Long id) {
        return getAdFile(id);
    }
}