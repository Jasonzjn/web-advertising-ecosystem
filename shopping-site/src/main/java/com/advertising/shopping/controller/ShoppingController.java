package com.advertising.shopping.controller;

import com.advertising.shopping.service.AdAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShoppingController {

    @Autowired
    private AdAPIService adAPIService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("websiteName", "购物网站");
        model.addAttribute("ads", adAPIService.getActiveAds());
        return "index";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("websiteName", "商品列表");
        model.addAttribute("ads", adAPIService.getActiveAds());
        return "products";
    }

    @GetMapping("/electronics")
    public String electronics(Model model) {
        model.addAttribute("websiteName", "电子产品");
        model.addAttribute("ads", adAPIService.getActiveAds());
        return "electronics";
    }

    @GetMapping("/clothing")
    public String clothing(Model model) {
        model.addAttribute("websiteName", "服装");
        model.addAttribute("ads", adAPIService.getActiveAds());
        return "clothing";
    }

    @GetMapping("/books")
    public String books(Model model) {
        model.addAttribute("websiteName", "图书");
        model.addAttribute("ads", adAPIService.getActiveAds());
        return "books";
    }
}