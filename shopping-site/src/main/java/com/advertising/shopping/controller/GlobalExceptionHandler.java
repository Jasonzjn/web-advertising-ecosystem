package com.advertising.shopping.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class GlobalExceptionHandler implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // 获取错误状态码
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        // 根据错误状态码进行处理
        if (statusCode != null) {
            if (statusCode == 404) {
                return "error"; // 返回现有的error.html页面
            } else if (statusCode == 500) {
                return "error"; // 返回现有的error.html页面
            }
        }

        return "error"; // 默认错误页面
    }

    public String getErrorPath() {
        return "/error";
    }
}