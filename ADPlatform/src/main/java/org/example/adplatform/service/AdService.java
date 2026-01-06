package org.example.adplatform.service;

import org.example.adplatform.model.Advertisement;
import org.example.adplatform.model.User;
import org.example.adplatform.repository.AdRepository;
import org.example.adplatform.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;

    // 最大文件大小（单位：MB）
    private static final long MAX_FILE_SIZE_MB = 10;

    // 允许的文件类型
    private static final String[] ALLOWED_FILE_TYPES = {
            "jpg", "jpeg", "png", "gif", "webp", "bmp",
            "mp4", "avi", "mov", "wmv", "flv",
            "html", "zip", "pdf", "txt"
    };

    public Advertisement createAd(String name, String description,
                                  MultipartFile file, User user) throws Exception {

        // 验证文件
        if (file.isEmpty()) {
            throw new RuntimeException("请选择要上传的文件");
        }

        // 验证文件大小
        FileUtils.validateFileSize(file, MAX_FILE_SIZE_MB);

        // 验证文件类型
        FileUtils.validateFileType(file, ALLOWED_FILE_TYPES);

        // 读取文件为字节数组
        byte[] fileData = FileUtils.readFileToBytes(file);

        // 确定广告类型
        Advertisement.AdType adType = FileUtils.determineAdType(file.getOriginalFilename());

        // 创建广告
        Advertisement ad = new Advertisement();
        ad.setName(name);
        ad.setDescription(description);
        ad.setFileData(fileData);
        ad.setOriginalFileName(file.getOriginalFilename());
        ad.setContentType(FileUtils.getContentType(file.getOriginalFilename()));
        ad.setFileSize(file.getSize());
        ad.setAdType(adType);
        ad.setUser(user);
        ad.setCreatedAt(LocalDateTime.now());
        ad.setActive(true);

        return adRepository.save(ad);
    }

    public Advertisement createTextAd(String name, String description,
                                      String textContent, User user) {
        Advertisement ad = new Advertisement();
        ad.setName(name);
        ad.setDescription(description);
        ad.setFileData(textContent.getBytes());
        ad.setOriginalFileName(name + ".txt");
        ad.setContentType("text/plain");
        ad.setFileSize((long) textContent.getBytes().length);
        ad.setAdType(Advertisement.AdType.TEXT);
        ad.setUser(user);
        ad.setCreatedAt(LocalDateTime.now());
        ad.setActive(true);

        return adRepository.save(ad);
    }

    public List<Advertisement> getUserAds(User user) {
        List<Advertisement> ads = adRepository.findByUser(user);

        // 对于大文件，移除文件数据以节省内存（仅保留元数据）
        ads.forEach(ad -> {
            if (ad.getFileSize() > 1024 * 1024) { // 大于1MB的文件
                ad.setFileData(null);
            }
        });

        return ads;
    }

    public List<Advertisement> getActiveAds() {
        List<Advertisement> ads = adRepository.findByActiveTrue();

        // 移除文件数据，只返回元数据
        ads.forEach(ad -> ad.setFileData(null));

        return ads;
    }

    public List<Advertisement> getActiveAdsByType(Advertisement.AdType adType) {
        List<Advertisement> ads = adRepository.findByAdTypeAndActiveTrue(adType);

        // 移除文件数据，只返回元数据
        ads.forEach(ad -> ad.setFileData(null));

        return ads;
    }

    public Advertisement getAdById(Long id) {
        return adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("广告不存在"));
    }

    public Advertisement getAdByIdWithFileData(Long id) {
        return getAdById(id); // 返回完整的广告数据（包括文件数据）
    }

    public Advertisement getAdMetadata(Long id) {
        Advertisement ad = getAdById(id);
        ad.setFileData(null); // 只返回元数据，不包含文件数据
        return ad;
    }

    public void incrementImpressions(Long adId) {
        Advertisement ad = getAdById(adId);
        ad.setImpressions(ad.getImpressions() + 1);
        adRepository.save(ad);
    }

    public void incrementClicks(Long adId) {
        Advertisement ad = getAdById(adId);
        ad.setClicks(ad.getClicks() + 1);
        adRepository.save(ad);
    }

    public void deleteAd(Long adId, User user) {
        Advertisement ad = getAdById(adId);

        // 检查权限
        if (!ad.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("无权删除此广告");
        }

        // 删除数据库记录（文件数据会自动删除）
        adRepository.delete(ad);
    }

    public Advertisement updateAd(Long adId, String name, String description,
                                  Boolean active, User user) {
        Advertisement ad = getAdById(adId);

        // 检查权限
        if (!ad.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("无权修改此广告");
        }

        if (name != null) {
            ad.setName(name);
        }
        if (description != null) {
            ad.setDescription(description);
        }
        if (active != null) {
            ad.setActive(active);
        }

        return adRepository.save(ad);
    }

    /**
     * 更新广告文件
     */
    public Advertisement updateAdFile(Long adId, MultipartFile file, User user) throws Exception {
        Advertisement ad = getAdById(adId);

        // 检查权限
        if (!ad.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("无权修改此广告");
        }

        // 验证文件
        FileUtils.validateFileSize(file, MAX_FILE_SIZE_MB);
        FileUtils.validateFileType(file, ALLOWED_FILE_TYPES);

        // 更新文件数据
        ad.setFileData(FileUtils.readFileToBytes(file));
        ad.setOriginalFileName(file.getOriginalFilename());
        ad.setContentType(FileUtils.getContentType(file.getOriginalFilename()));
        ad.setFileSize(file.getSize());
        ad.setAdType(FileUtils.determineAdType(file.getOriginalFilename()));

        return adRepository.save(ad);
    }

    /**
     * 获取广告统计信息
     */
    public long getTotalAds() {
        return adRepository.count();
    }

    public long getTotalAdsByUser(User user) {
        return adRepository.countByUser(user);
    }

    public long getTotalFileSizeByUser(User user) {
        List<Advertisement> ads = adRepository.findByUser(user);
        return ads.stream()
                .mapToLong(Advertisement::getFileSize)
                .sum();
    }
}