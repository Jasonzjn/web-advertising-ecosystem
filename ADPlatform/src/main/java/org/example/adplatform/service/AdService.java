package org.example.adplatform.service;

import org.example.adplatform.model.Advertisement;
import org.example.adplatform.model.User;
import org.example.adplatform.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdService {
    @Autowired
    private AdRepository adRepository;

    public Advertisement createAd(String name, String content,
                                  Advertisement.AdType adType, User user) {
        Advertisement ad = new Advertisement();
        ad.setName(name);
        ad.setContent(content);
        ad.setAdType(adType);
        ad.setUser(user);
        ad.setCreatedAt(LocalDateTime.now());
        ad.setActive(true);

        return adRepository.save(ad);
    }

    public List<Advertisement> getUserAds(User user) {
        return adRepository.findByUser(user);
    }

    public List<Advertisement> getActiveAds() {
        return adRepository.findByActiveTrue();
    }

    public Advertisement getAdById(Long id) {
        return adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("广告不存在"));
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
}
