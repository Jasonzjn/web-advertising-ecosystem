package com.advertising.shopping.controller;

import com.advertising.shopping.entity.ProductType;
import com.advertising.shopping.service.AdAPIService;
import com.advertising.shopping.service.CartService;
import com.advertising.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class ShoppingController {

    @Autowired
    private AdAPIService adAPIService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("websiteName", "购物网站");
        model.addAttribute("ads", adAPIService.getActiveAds());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("cart", cartService.getCart(session));
        return "index";
    }

    @GetMapping("/products")
    public String products(Model model, HttpSession session) {
        model.addAttribute("websiteName", "商品列表");
        model.addAttribute("ads", adAPIService.getActiveAds());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("cart", cartService.getCart(session));
        return "products";
    }

    @GetMapping("/electronics")
    public String electronics(Model model, HttpSession session) {
        model.addAttribute("websiteName", "电子产品");
        model.addAttribute("ads", adAPIService.getActiveAds());
        model.addAttribute("products", productService.getProductsByType(ProductType.ELECTRONIC));
        model.addAttribute("cart", cartService.getCart(session));
        return "electronics";
    }

    @GetMapping("/clothing")
    public String clothing(Model model, HttpSession session) {
        model.addAttribute("websiteName", "服装");
        model.addAttribute("ads", adAPIService.getActiveAds());
        model.addAttribute("products", productService.getProductsByType(ProductType.CLOTHING));
        model.addAttribute("cart", cartService.getCart(session));
        return "clothing";
    }

    @GetMapping("/books")
    public String books(Model model, HttpSession session) {
        model.addAttribute("websiteName", "图书");
        model.addAttribute("ads", adAPIService.getActiveAds());
        model.addAttribute("products", productService.getProductsByType(ProductType.FOOD)); // 用食品类商品作为图书的替代
        model.addAttribute("cart", cartService.getCart(session));
        return "books";
    }

    @GetMapping("/type/{type}")
    public String productsByType(@PathVariable String type, Model model, HttpSession session) {
        ProductType productType = ProductType.valueOf(type.toUpperCase());
        model.addAttribute("websiteName", getProductTypeName(productType) + "商品");
        model.addAttribute("ads", adAPIService.getActiveAds());
        model.addAttribute("products", productService.getProductsByType(productType));
        model.addAttribute("cart", cartService.getCart(session));
        return "products";
    }

    private String getProductTypeName(ProductType type) {
        switch (type) {
            case SPORT: return "运动";
            case ELECTRONIC: return "电子";
            case CLOTHING: return "服装";
            case FOOD: return "食品";
            default: return "商品";
        }
    }
}