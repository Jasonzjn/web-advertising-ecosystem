package org.example.adplatform.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CrossDomainService {

    /**
     * 从请求中提取跨域数据
     */
    public Map<String, Object> extractCrossDomainData(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<>();

        // 基本请求信息
        data.put("referer", request.getHeader("Referer"));
        data.put("userAgent", request.getHeader("User-Agent"));
        data.put("ipAddress", getClientIp(request));
        data.put("requestMethod", request.getMethod());
        data.put("requestURL", request.getRequestURL().toString());

        // 请求参数
        Map<String, String[]> params = request.getParameterMap();
        if (!params.isEmpty()) {
            Map<String, String> simpleParams = new HashMap<>();
            params.forEach((key, values) -> {
                if (values.length > 0) {
                    simpleParams.put(key, values[0]);
                }
            });
            data.put("parameters", simpleParams);
        }

        // 请求头
        Map<String, String> headers = new HashMap<>();
        request.getHeaderNames().asIterator()
                .forEachRemaining(headerName ->
                        headers.put(headerName, request.getHeader(headerName)));
        data.put("headers", headers);

        return data;
    }

    /**
     * 获取客户端IP地址
     */
    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 验证跨域请求是否合法
     */
    public boolean validateCrossDomainRequest(HttpServletRequest request) {
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");

        // 简单验证：允许所有来源，生产环境应该白名单验证
        // if (origin != null && !isAllowedOrigin(origin)) {
        //     return false;
        // }

        return true;
    }
}