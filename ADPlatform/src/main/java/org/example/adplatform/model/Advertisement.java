package org.example.adplatform.model;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "advertisements")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String content; // 存储图片URL或视频URL

    @Enumerated(EnumType.STRING)
    private AdType adType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime createdAt;
    private boolean active = true;
    private int impressions = 0;
    private int clicks = 0;

    public enum AdType {
        BANNER, // 横幅广告
        VIDEO,  // 视频广告
        TEXT,   // 文字广告
        POPUP   // 弹窗广告
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public AdType getAdType() { return adType; }
    public void setAdType(AdType adType) { this.adType = adType; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public int getImpressions() { return impressions; }
    public void setImpressions(int impressions) { this.impressions = impressions; }

    public int getClicks() { return clicks; }
    public void setClicks(int clicks) { this.clicks = clicks; }
}
