package com.advertising.video.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 处理 /videos/** 路径的请求，映射到静态资源
        registry.addResourceHandler("/videos/**")
                .addResourceLocations("classpath:/static/videos/")
                .setCachePeriod(3600);
    }
}