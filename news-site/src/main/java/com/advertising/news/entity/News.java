package com.advertising.news.entity;

import java.time.LocalDateTime;

public class News {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String publishDate;
    private NewsType type;
    private String imageUrl; // 添加图片URL字段

    public News() {}

    public News(Long id, String title, String content, String author, String publishDate, NewsType type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.publishDate = publishDate;
        this.type = type;
        // 设置默认图片URL
        this.imageUrl = getDefaultImageUrl(type);
    }

    public News(Long id, String title, String content, String author, String publishDate, NewsType type, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.publishDate = publishDate;
        this.type = type;
        this.imageUrl = imageUrl;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public NewsType getType() {
        return type;
    }

    public void setType(NewsType type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String getDefaultImageUrl(NewsType type) {
        switch (type) {
            case SPORT:
                return "https://via.placeholder.com/800x400?text=体育新闻图片";
            case ELECTRONIC:
                return "https://via.placeholder.com/800x400?text=科技新闻图片";
            case CLOTHING:
                return "https://via.placeholder.com/800x400?text=时尚新闻图片";
            case FOOD:
                return "https://via.placeholder.com/800x400?text=美食新闻图片";
            default:
                return "https://via.placeholder.com/800x400?text=新闻图片";
        }
    }
}