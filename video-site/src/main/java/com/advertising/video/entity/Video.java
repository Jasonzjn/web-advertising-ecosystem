package com.advertising.video.entity;

import java.time.LocalDateTime;

public class Video {
    private Long id;
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private VideoType type;
    private LocalDateTime uploadTime;

    public Video() {}

    public Video(Long id, String title, String description, String videoUrl, String thumbnailUrl, VideoType type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.type = type;
        this.uploadTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public VideoType getType() {
        return type;
    }

    public void setType(VideoType type) {
        this.type = type;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }
}