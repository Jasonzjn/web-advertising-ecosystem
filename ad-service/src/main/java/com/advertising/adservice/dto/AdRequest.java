package com.advertising.adservice.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class AdRequest {
    @NotBlank
    private String title;

    private String content;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String targetUrl;

    @NotBlank
    private String targetWebsite; // "news", "shopping", or "both"

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    // Constructors
    public AdRequest() {}

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTargetWebsite() {
        return targetWebsite;
    }

    public void setTargetWebsite(String targetWebsite) {
        this.targetWebsite = targetWebsite;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}