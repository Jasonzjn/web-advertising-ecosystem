package org.example.adplatform.model;

public class CrossDomainRequest {

    private String referrer;      // 来源网站
    private String userAgent;     // 用户代理
    private String ipAddress;     // IP地址
    private String screenSize;    // 屏幕尺寸
    private String language;      // 语言
    private String customData;    // 自定义数据（JSON格式）

    // Getters and Setters
    public String getReferrer() { return referrer; }
    public void setReferrer(String referrer) { this.referrer = referrer; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getScreenSize() { return screenSize; }
    public void setScreenSize(String screenSize) { this.screenSize = screenSize; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getCustomData() { return customData; }
    public void setCustomData(String customData) { this.customData = customData; }
}