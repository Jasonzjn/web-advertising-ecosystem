package org.example.adplatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig {

    /*private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://10.100.164.30"

    );*/
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // 允许所有API端点跨域访问
                registry.addMapping("/api/**")
                        .allowedOriginPatterns("*")  // 允许所有域名，生产环境应该指定具体域名
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)  // 允许发送Cookie
                        .maxAge(3600);  // 预检请求缓存时间

                // 允许公共广告预览端点跨域
                registry.addMapping("/public/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
