package org.example.adplatform.controller;

import org.example.adplatform.model.Advertisement;
import org.example.adplatform.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/public")
public class ApiController {

    @Autowired
    private AdService adService;

    /**
     * 获取随机广告（Base64格式）
     */
    @GetMapping("/ad/random")
    public ResponseEntity<?> getRandomAd(@RequestParam(required = false) String type,
                                         @RequestParam(defaultValue = "false") boolean base64) {
        try {
            List<Advertisement> ads;

            if (type != null && !type.isEmpty()) {
                Advertisement.AdType adType = Advertisement.AdType.valueOf(type.toUpperCase());
                ads = adService.getActiveAdsByType(adType);
            } else {
                ads = adService.getActiveAds();
            }

            if (ads.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // 随机选择一个广告
            Random random = new Random();
            Advertisement ad = ads.get(random.nextInt(ads.size()));

            // 获取完整的广告数据（包含文件数据）
            ad = adService.getAdByIdWithFileData(ad.getId());

            // 增加展示次数
            adService.incrementImpressions(ad.getId());

            if (base64) {
                // 返回Base64格式
                return getAdBase64Response(ad);
            } else {
                // 根据文件类型返回不同的响应
                return getAdResponse(ad);
            }

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 构建Base64广告响应
     */
    private ResponseEntity<?> getAdBase64Response(Advertisement ad) {
        try {
            if (ad.getFileData() == null) {
                throw new RuntimeException("文件数据不存在");
            }

            // 将文件数据转换为Base64
            String base64Data = Base64.getEncoder().encodeToString(ad.getFileData());

            Map<String, Object> response = new HashMap<>();
            response.put("id", ad.getId());
            response.put("name", ad.getName());
            response.put("description", ad.getDescription());
            response.put("fileName", ad.getOriginalFileName());
            response.put("contentType", ad.getContentType());
            response.put("fileSize", ad.getFileSize());
            response.put("adType", ad.getAdType().toString());
            response.put("impressions", ad.getImpressions());
            response.put("clicks", ad.getClicks());
            response.put("base64Data", base64Data);
            response.put("dataUri", "data:" + ad.getContentType() + ";base64," + base64Data);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 构建广告响应（JSON格式）
     */
    private ResponseEntity<?> getAdResponse(Advertisement ad) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", ad.getId());
        response.put("name", ad.getName());
        response.put("description", ad.getDescription());
        response.put("fileName", ad.getOriginalFileName());
        response.put("contentType", ad.getContentType());
        response.put("fileSize", ad.getFileSize());
        response.put("adType", ad.getAdType().toString());
        response.put("impressions", ad.getImpressions());
        response.put("clicks", ad.getClicks());

        // 根据文件大小决定是否包含文件数据
        if (ad.getFileData() != null && ad.getFileSize() <= 1024 * 1024) { // 小于1MB的文件
            String base64Data = Base64.getEncoder().encodeToString(ad.getFileData());
            response.put("data", base64Data);
            response.put("dataUri", "data:" + ad.getContentType() + ";base64," + base64Data);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 获取指定广告
     */
    @GetMapping("/ad/{id}")
    public ResponseEntity<?> getAdById(@PathVariable Long id,
                                       @RequestParam(defaultValue = "false") boolean base64) {
        try {
            Advertisement ad = adService.getAdByIdWithFileData(id);

            // 增加展示次数
            adService.incrementImpressions(id);

            if (!ad.isActive()) {
                return ResponseEntity.status(404).body("广告已暂停");
            }

            if (base64) {
                return getAdBase64Response(ad);
            } else {
                return getAdResponse(ad);
            }

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 获取广告文件（直接返回文件流）
     */
    @GetMapping("/ad/{id}/file")
    public ResponseEntity<?> getAdFile(@PathVariable Long id) {
        try {
            Advertisement ad = adService.getAdByIdWithFileData(id);

            if (!ad.isActive()) {
                return ResponseEntity.status(404).body("广告已暂停");
            }

            if (ad.getFileData() == null) {
                return ResponseEntity.notFound().build();
            }

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(ad.getContentType()));
            headers.set("Content-Disposition", "inline; filename=\"" + ad.getOriginalFileName() + "\"");

            // 增加展示次数
            adService.incrementImpressions(id);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(ad.getFileData());

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 记录点击
     */
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

    /**
     * 获取广告元数据（不含文件数据）
     */
    @GetMapping("/ad/{id}/metadata")
    public ResponseEntity<?> getAdMetadata(@PathVariable Long id) {
        try {
            Advertisement ad = adService.getAdMetadata(id);

            if (!ad.isActive()) {
                return ResponseEntity.status(404).body("广告已暂停");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("id", ad.getId());
            response.put("name", ad.getName());
            response.put("description", ad.getDescription());
            response.put("fileName", ad.getOriginalFileName());
            response.put("contentType", ad.getContentType());
            response.put("fileSize", ad.getFileSize());
            response.put("adType", ad.getAdType().toString());
            response.put("impressions", ad.getImpressions());
            response.put("clicks", ad.getClicks());
            response.put("createdAt", ad.getCreatedAt());

            // 增加展示次数
            adService.incrementImpressions(id);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 获取广告嵌入代码（使用Base64内联数据）
     */
    @GetMapping("/ad/{id}/embed")
    public ResponseEntity<?> getEmbedCode(@PathVariable Long id,
                                          @RequestParam(defaultValue = "300") int width,
                                          @RequestParam(defaultValue = "250") int height) {
        try {
            Advertisement ad = adService.getAdByIdWithFileData(id);

            if (!ad.isActive()) {
                return ResponseEntity.status(404).body("广告已暂停");
            }

            String embedCode = generateEmbedCode(ad, width, height);

            Map<String, Object> response = new HashMap<>();
            response.put("id", ad.getId());
            response.put("name", ad.getName());
            response.put("embedCode", embedCode);

            // 增加展示次数
            adService.incrementImpressions(id);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * 生成嵌入代码（使用Base64数据URI）
     */
    private String generateEmbedCode(Advertisement ad, int width, int height) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!-- Advertisement: ").append(ad.getName()).append(" -->\n");
        sb.append("<div id='ad-container-").append(ad.getId()).append("' ");
        sb.append("style='width:").append(width).append("px; height:").append(height).append("px; ");
        sb.append("border:1px solid #ccc; overflow:hidden; cursor:pointer;'>\n");

        if (ad.getFileData() == null) {
            sb.append("  <p>广告数据加载失败</p>\n");
            sb.append("</div>");
            return sb.toString();
        }

        // 将文件数据转换为Base64
        String base64Data = Base64.getEncoder().encodeToString(ad.getFileData());

        switch (ad.getAdType()) {
            case IMAGE:
            case GIF:
                String imageSrc = "data:" + ad.getContentType() + ";base64," + base64Data;
                sb.append("  <img src='").append(imageSrc).append("' ");
                sb.append("alt='").append(ad.getName()).append("' ");
                sb.append("style='width:100%; height:100%; object-fit:contain;' ");
                sb.append("onclick='adClick(").append(ad.getId()).append(")'>\n");
                break;

            case VIDEO:
                sb.append("  <video width='100%' height='100%' controls ");
                sb.append("onclick='adClick(").append(ad.getId()).append(")'>\n");
                sb.append("    <source src='data:").append(ad.getContentType()).append(";base64,");
                sb.append(base64Data).append("' type='").append(ad.getContentType()).append("'>\n");
                sb.append("    您的浏览器不支持视频标签。\n");
                sb.append("  </video>\n");
                break;

            case TEXT:
                String textContent = new String(ad.getFileData());
                sb.append("  <div onclick='adClick(").append(ad.getId()).append(")' ");
                sb.append("style='padding:10px;'>\n");
                sb.append("    <h4>").append(ad.getName()).append("</h4>\n");
                sb.append("    <p>").append(textContent).append("</p>\n");
                sb.append("  </div>\n");
                break;

            case HTML5:
                // 对于HTML5广告，假设文件是zip或html
                sb.append("  <iframe srcdoc='");
                String htmlContent = new String(ad.getFileData());
                // 转义HTML内容
                htmlContent = htmlContent.replace("'", "\\'")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r");
                sb.append(htmlContent);
                sb.append("' style='width:100%; height:100%; border:none;'></iframe>\n");
                break;

            default:
                sb.append("  <p>不支持的广告类型</p>\n");
        }

        sb.append("</div>\n");
        sb.append("<script>\n");
        sb.append("function adClick(adId) {\n");
        sb.append("  // 记录点击\n");
        sb.append("  fetch('http://localhost:8080/ad-platform/api/public/ad/' + adId + '/click', {\n");
        sb.append("    method: 'POST',\n");
        sb.append("    mode: 'cors'\n");
        sb.append("  });\n");
        sb.append("  // 可以在这里添加跳转逻辑\n");
        sb.append("  return false;\n");
        sb.append("}\n");
        sb.append("</script>");

        return sb.toString();
    }
}
