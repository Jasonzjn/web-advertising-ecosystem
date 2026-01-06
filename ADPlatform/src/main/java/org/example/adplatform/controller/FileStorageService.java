package org.example.adplatform.controller;

import org.example.adplatform.model.Advertisement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * 存储上传的文件
     */
    public String storeFile(MultipartFile file) throws IOException {
        // 创建存储目录
        String datePath = LocalDateTime.now().format(DATE_FORMATTER);
        Path targetLocation = Paths.get(uploadDir, datePath);
        Files.createDirectories(targetLocation);

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;

        // 保存文件
        Path filePath = targetLocation.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // 返回相对路径
        return Paths.get(datePath, uniqueFileName).toString();
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 根据文件扩展名判断广告类型
     */
    public Advertisement.AdType determineAdType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();

        switch (extension) {
            case "jpg":
            case "jpeg":
            case "png":
            case "webp":
            case "bmp":
                return Advertisement.AdType.IMAGE;

            case "mp4":
            case "avi":
            case "mov":
            case "wmv":
            case "flv":
                return Advertisement.AdType.VIDEO;

            case "gif":
                return Advertisement.AdType.GIF;

            case "zip":
            case "html":
                return Advertisement.AdType.HTML5;

            default:
                return Advertisement.AdType.IMAGE;
        }
    }

    /**
     * 获取文件MIME类型
     */
    public String getContentType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();

        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "mp4":
                return "video/mp4";
            case "webp":
                return "image/webp";
            case "html":
                return "text/html";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * 删除文件
     */
    public boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(uploadDir, filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }
}