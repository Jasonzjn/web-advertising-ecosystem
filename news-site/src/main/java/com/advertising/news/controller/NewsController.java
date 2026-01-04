package com.advertising.news.controller;

import com.advertising.news.service.AdAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewsController {

    @Autowired
    private AdAPIService adAPIService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("websiteName", "新闻网站");
        model.addAttribute("ads", adAPIService.getActiveAds());
        return "index";
    }

    @GetMapping("/news")
    public String news(Model model) {
        model.addAttribute("websiteName", "新闻网站");
        model.addAttribute("ads", adAPIService.getActiveAds());
        return "news";
    }

    @GetMapping("/politics")
    public String politics(Model model) {
        model.addAttribute("websiteName", "政治新闻");
        model.addAttribute("ads", adAPIService.getActiveAds());
        return "politics";
    }

    @GetMapping("/sports")
    public String sports(Model model) {
        model.addAttribute("websiteName", "体育新闻");
        model.addAttribute("ads", adAPIService.getActiveAds());
        return "sports";
    }

    @GetMapping("/technology")
    public String technology(Model model) {
        model.addAttribute("websiteName", "科技新闻");
        model.addAttribute("ads", adAPIService.getActiveAds());
        return "technology";
    }
}