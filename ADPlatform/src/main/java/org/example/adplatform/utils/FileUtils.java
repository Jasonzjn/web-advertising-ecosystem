package org.example.adplatform.utils;


import org.example.adplatform.model.Advertisement;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Arrays;

public class FileUtils {

    // 支持的文件类型
    private static final String[] IMAGE_TYPES = {"jpg", "jpeg", "png", "webp", "bmp"};
    private static final String[] VIDEO_TYPES = {"mp4", "avi", "mov", "wmv", "flv"};
    private static final String[] DOCUMENT_TYPES = {"pdf", "doc", "docx"};

    /**
     * 根据文件名确定广告类型
     */
    public static Advertisement.AdType determineAdType(String filename) {
        String extension = getFileExtension(filename).toLowerCase();

        if (Arrays.asList(IMAGE_TYPES).contains(extension)) {
            return Advertisement.AdType.IMAGE;
        } else if (Arrays.asList(VIDEO_TYPES).contains(extension)) {
            return Advertisement.AdType.VIDEO;
        } else if ("gif".equals(extension)) {
            return Advertisement.AdType.GIF;
        } else if ("html".equals(extension) || "zip".equals(extension)) {
            return Advertisement.AdType.HTML5;
        } else {
            return Advertisement.AdType.TEXT;
        }
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 获取文件MIME类型
     */
    public static String getContentType(String filename) {
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
            case "pdf":
                return "application/pdf";
            case "html":
                return "text/html";
            case "zip":
                return "application/zip";
            default:
                return "application/octet-stream";
        }
    }

    /**
     * 验证文件大小
     */
    public static void validateFileSize(MultipartFile file, long maxSizeMB) throws Exception {
        long maxSize = maxSizeMB * 1024 * 1024; // 转换为字节
        if (file.getSize() > maxSize) {
            throw new Exception("文件大小不能超过 " + maxSizeMB + " MB");
        }
    }

    /**
     * 验证文件类型
     */
    public static void validateFileType(MultipartFile file, String[] allowedTypes) throws Exception {
        String extension = getFileExtension(file.getOriginalFilename());
        boolean allowed = false;

        for (String type : allowedTypes) {
            if (type.equalsIgnoreCase(extension)) {
                allowed = true;
                break;
            }
        }

        if (!allowed) {
            throw new Exception("不支持的文件类型: " + extension);
        }
    }

    /**
     * 读取文件为字节数组
     */
    public static byte[] readFileToBytes(MultipartFile file) throws IOException {
        return file.getBytes();
    }

    /**
     * 获取文件大小描述
     */
    public static String getFileSizeDescription(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        }
    }
}
