package org.example.adplatform.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Optional;

public class CookieUtils {

    /**
     * 创建Cookie
     */
    public static Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);  // 单位：秒
        return cookie;
    }

    /**
     * 创建跨域Cookie（支持第三方网站）
     */
    public static Cookie createCrossDomainCookie(String name, String value, int maxAge, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(false);  // 允许JavaScript访问
        cookie.setMaxAge(maxAge);
        if (domain != null && !domain.isEmpty()) {
            cookie.setDomain(domain);
        }
        return cookie;
    }

    /**
     * 创建安全Cookie
     */
    public static Cookie createSecureCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);  // 仅HTTPS
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    /**
     * 从请求中获取Cookie值
     */
    public static Optional<String> getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

    /**
     * 删除Cookie
     */
    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);  // 立即过期
        response.addCookie(cookie);
    }
}
