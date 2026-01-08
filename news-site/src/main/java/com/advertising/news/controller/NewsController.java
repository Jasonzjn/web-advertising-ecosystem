package com.advertising.news.controller;

import com.advertising.news.entity.News;
import com.advertising.news.entity.NewsType;
import com.advertising.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class NewsController {



    @Autowired
    private NewsService newsService;

    @GetMapping("/")
    public String home(Model model) {
        // 重定向到 /news 路径，因为首页应该是 /news
        return "redirect:/news";
    }

    @GetMapping("/news")
    public String news(Model model) {
        model.addAttribute("websiteName", "新闻网站");

        model.addAttribute("newsList", newsService.getAllNews());
        return "news";
    }

    @GetMapping("/news/{id}")
    public String newsDetail(@PathVariable Long id, Model model) {
        News news = newsService.getNewsById(id);
        if (news == null) {
            model.addAttribute("errorMessage", "新闻不存在");
            return "error";
        }

        model.addAttribute("websiteName", news.getTitle() + " - 新闻详情");
        model.addAttribute("news", news);

        // 获取同类型的其他新闻作为相关新闻
        List<News> relatedNews = newsService.getNewsByType(news.getType()).stream()
                .filter(n -> !n.getId().equals(id))
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("relatedNews", relatedNews);

        // 获取热门新闻（这里简单地取最新的几条）
        List<News> popularNews = newsService.getAllNews().stream()
                .filter(n -> !n.getId().equals(id))
                .limit(5)
                .collect(Collectors.toList());
        model.addAttribute("popularNews", popularNews);

        return "news_detail";
    }

    @GetMapping("/politics")
    public String politics(Model model) {
        model.addAttribute("websiteName", "政治新闻");
        // 修复：使用SPORT类新闻作为政治新闻的替代，因为目前没有政治类新闻
        model.addAttribute("newsList", newsService.getNewsByType(NewsType.SPORT));
        return "politics";
    }

    @GetMapping("/sports")
    public String sports(Model model) {
        model.addAttribute("websiteName", "体育新闻");
        model.addAttribute("newsList", newsService.getNewsByType(NewsType.SPORT));
        return "sports";
    }

    @GetMapping("/technology")
    public String technology(Model model) {
        model.addAttribute("websiteName", "科技新闻");
        model.addAttribute("newsList", newsService.getNewsByType(NewsType.ELECTRONIC));
        return "technology";
    }

    @GetMapping("/type/{type}")
    public String newsByType(@PathVariable String type, Model model) {
        NewsType newsType;
        try {
            newsType = NewsType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 如果传入的类型不存在，返回错误页面
            model.addAttribute("errorMessage", "新闻类型不存在: " + type);
            return "error";
        }

        model.addAttribute("websiteName", getNewsTypeName(newsType) + "新闻");
        model.addAttribute("newsList", newsService.getNewsByType(newsType));
        return "news";
    }

    private String getNewsTypeName(NewsType type) {
        switch (type) {
            case SPORT: return "体育";
            case ELECTRONIC: return "电子科技";
            case CLOTHING: return "时尚";
            case FOOD: return "美食";
            default: return "新闻";
        }
    }

    // 添加异常处理
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(Exception e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }
}