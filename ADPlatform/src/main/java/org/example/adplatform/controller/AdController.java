package org.example.adplatform.controller;

import org.example.adplatform.model.Advertisement;
import org.example.adplatform.model.User;
import org.example.adplatform.service.AdService;
import org.example.adplatform.service.AuthService;
import org.example.adplatform.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ads")
public class AdController {

    @Autowired
    private AdService adService;

    @Autowired
    private AuthService authService;

    /**
     * 上传广告文件（二进制存储）
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAd(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("file") MultipartFile file) {

        try {
            // 验证用户
            User user = authService.login(username, password);

            // 创建广告
            Advertisement ad = adService.createAd(name, description, file, user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "广告上传成功");
            response.put("adId", ad.getId());
            response.put("name", ad.getName());
            response.put("adType", ad.getAdType().toString());
            response.put("fileSize", ad.getFileSize());
            response.put("fileName", ad.getOriginalFileName());
            response.put("contentType", ad.getContentType());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 创建文字广告
     */
    @PostMapping("/create-text")
    public ResponseEntity<?> createTextAd(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            String password = (String) request.get("password");

            User user = authService.login(username, password);

            String name = (String) request.get("name");
            String description = (String) request.get("description");
            String textContent = (String) request.get("textContent");

            Advertisement ad = adService.createTextAd(name, description, textContent, user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "文字广告创建成功");
            response.put("adId", ad.getId());
            response.put("name", ad.getName());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 获取用户的广告列表（不含文件数据）
     */
    @GetMapping("/my-ads")
    public ResponseEntity<?> getMyAds(@RequestParam String username,
                                      @RequestParam String password) {
        try {
            User user = authService.login(username, password);
            List<Advertisement> ads = adService.getUserAds(user);

            // 转换为响应格式
            List<Map<String, Object>> adList = ads.stream()
                    .map(ad -> {
                        Map<String, Object> adMap = new HashMap<>();
                        adMap.put("id", ad.getId());
                        adMap.put("name", ad.getName());
                        adMap.put("description", ad.getDescription());
                        adMap.put("fileName", ad.getOriginalFileName());
                        adMap.put("contentType", ad.getContentType());
                        adMap.put("fileSize", ad.getFileSize());
                        adMap.put("fileSizeDesc", FileUtils.getFileSizeDescription(ad.getFileSize()));
                        adMap.put("adType", ad.getAdType().toString());
                        adMap.put("impressions", ad.getImpressions());
                        adMap.put("clicks", ad.getClicks());
                        adMap.put("active", ad.isActive());
                        adMap.put("createdAt", ad.getCreatedAt());
                        return adMap;
                    })
                    .toList();

            return ResponseEntity.ok(adList);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 下载广告文件
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadAdFile(@PathVariable Long id) {
        try {
            Advertisement ad = adService.getAdByIdWithFileData(id);

            if (ad.getFileData() == null) {
                throw new RuntimeException("文件数据不存在");
            }

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(ad.getContentType()));
            headers.setContentDispositionFormData("attachment", ad.getOriginalFileName());
            headers.setContentLength(ad.getFileData().length);

            return new ResponseEntity<>(ad.getFileData(), headers, HttpStatus.OK);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "文件下载失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 获取广告文件（Base64编码）
     */
    @GetMapping("/{id}/file")
    public ResponseEntity<?> getAdFileBase64(@PathVariable Long id) {
        try {
            Advertisement ad = adService.getAdByIdWithFileData(id);

            if (ad.getFileData() == null) {
                throw new RuntimeException("文件数据不存在");
            }

            // 将文件数据转换为Base64
            String base64Data = Base64.getEncoder().encodeToString(ad.getFileData());

            Map<String, Object> response = new HashMap<>();
            response.put("id", ad.getId());
            response.put("name", ad.getName());
            response.put("fileName", ad.getOriginalFileName());
            response.put("contentType", ad.getContentType());
            response.put("adType", ad.getAdType().toString());
            response.put("base64Data", base64Data);

            // 增加展示次数
            adService.incrementImpressions(id);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "获取文件失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 获取广告元数据（不含文件数据）
     */
    @GetMapping("/{id}/metadata")
    public ResponseEntity<?> getAdMetadata(@PathVariable Long id) {
        try {
            Advertisement ad = adService.getAdMetadata(id);

            Map<String, Object> response = new HashMap<>();
            response.put("id", ad.getId());
            response.put("name", ad.getName());
            response.put("description", ad.getDescription());
            response.put("fileName", ad.getOriginalFileName());
            response.put("contentType", ad.getContentType());
            response.put("fileSize", ad.getFileSize());
            response.put("fileSizeDesc", FileUtils.getFileSizeDescription(ad.getFileSize()));
            response.put("adType", ad.getAdType().toString());
            response.put("impressions", ad.getImpressions());
            response.put("clicks", ad.getClicks());
            response.put("active", ad.isActive());
            response.put("createdAt", ad.getCreatedAt());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 删除广告
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Long id,
                                      @RequestParam String username,
                                      @RequestParam String password) {
        try {
            User user = authService.login(username, password);
            adService.deleteAd(id, user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "广告删除成功");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 更新广告信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable Long id,
                                      @RequestBody Map<String, Object> request,
                                      @RequestParam String username,
                                      @RequestParam String password) {
        try {
            User user = authService.login(username, password);

            String name = (String) request.get("name");
            String description = (String) request.get("description");
            Boolean active = (Boolean) request.get("active");

            Advertisement ad = adService.updateAd(id, name, description, active, user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "广告更新成功");
            response.put("adId", ad.getId());
            response.put("name", ad.getName());
            response.put("active", ad.isActive());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 更新广告文件
     */
    @PutMapping(value = "/{id}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAdFile(@PathVariable Long id,
                                          @RequestParam String username,
                                          @RequestParam String password,
                                          @RequestParam("file") MultipartFile file) {
        try {
            User user = authService.login(username, password);

            Advertisement ad = adService.updateAdFile(id, file, user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "广告文件更新成功");
            response.put("adId", ad.getId());
            response.put("name", ad.getName());
            response.put("fileName", ad.getOriginalFileName());
            response.put("fileSize", ad.getFileSize());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 获取用户统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getUserStats(@RequestParam String username,
                                          @RequestParam String password) {
        try {
            User user = authService.login(username, password);

            long totalAds = adService.getTotalAdsByUser(user);
            long totalFileSize = adService.getTotalFileSizeByUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("totalAds", totalAds);
            response.put("totalFileSize", totalFileSize);
            response.put("totalFileSizeDesc", FileUtils.getFileSizeDescription(totalFileSize));
            response.put("userId", user.getId());
            response.put("username", user.getUsername());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}