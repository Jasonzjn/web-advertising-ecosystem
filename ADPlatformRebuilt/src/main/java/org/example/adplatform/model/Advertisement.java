package org.example.adplatform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "advertisements")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // 核心修改：直接存储文件的二进制数据
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] fileData;  // 改为 fileData 更清晰

    // 文件元数据
    private String fileName;
    private String fileType;  // 文件类型：image/jpeg, video/mp4 等
    private Long fileSize;    // 文件大小（字节）

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;

    public enum Category {
        SPORT, ELECTRONIC, CLOTHING, FOOD
    }

    // 辅助方法：获取Base64编码
    @Transient
    public String getBase64Data() {
        if (fileData == null) return "";
        return java.util.Base64.getEncoder().encodeToString(fileData);
    }

    // 辅助方法：获取Data URI
    @Transient
    public String getDataUri() {
        if (fileData == null || fileType == null) return "";
        return "data:" + fileType + ";base64," + getBase64Data();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}