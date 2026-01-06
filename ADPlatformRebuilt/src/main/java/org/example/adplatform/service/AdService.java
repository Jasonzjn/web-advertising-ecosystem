package org.example.adplatform.service;

import org.example.adplatform.model.*;
import org.example.adplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdService {

    @Autowired
    private AdRepository adRepository;

    public Advertisement createAd(String name, MultipartFile file,
                                  Advertisement.Category category, User user)
            throws IOException {
        Advertisement ad = new Advertisement();
        ad.setName(name);

        // 核心修改：直接存储文件的二进制数据
        ad.setFileData(file.getBytes());  // 将文件转为字节数组
        ad.setFileName(file.getOriginalFilename());
        ad.setFileType(file.getContentType());
        ad.setFileSize(file.getSize());

        ad.setCategory(category);
        ad.setUser(user);
        ad.setCreatedAt(LocalDateTime.now());

        return adRepository.save(ad);
    }

    public List<Advertisement> getAdsByCategory(Advertisement.Category category) {
        return adRepository.findByCategory(category);
    }

    public Advertisement getAdById(Long id) {
        return adRepository.findById(id).orElse(null);
    }

    public List<Advertisement> getAllAds() {
        return adRepository.findAll();
    }

    // 新方法：获取广告的二进制数据
    public byte[] getAdFileData(Long id) {
        Advertisement ad = getAdById(id);
        return ad != null ? ad.getFileData() : null;
    }

    // 新方法：获取广告的Base64数据
    public String getAdBase64Data(Long id) {
        Advertisement ad = getAdById(id);
        return ad != null ? ad.getBase64Data() : null;
    }

    // 新方法：获取广告的Data URI
    public String getAdDataUri(Long id) {
        Advertisement ad = getAdById(id);
        return ad != null ? ad.getDataUri() : null;
    }
}