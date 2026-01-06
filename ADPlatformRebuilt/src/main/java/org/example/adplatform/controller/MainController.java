package org.example.adplatform.controller;

import jakarta.servlet.http.HttpSession;
import org.example.adplatform.model.*;
import org.example.adplatform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AdService adService;

    // 首页
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("loggedIn", user != null);
        return "index";
    }

    // 注册页面
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // 处理注册
    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           Model model) {
        boolean success = authService.register(username, password, email);
        if (success) {
            model.addAttribute("message", "注册成功，请登录");
            return "login";
        } else {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }
    }

    // 登录页面
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // 处理登录
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        User user = authService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/upload";
        } else {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }

    // 登出
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // 上传广告页面
    @GetMapping("/upload")
    public String uploadPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("categories", Advertisement.Category.values());
        return "upload";
    }

    // 处理广告上传
    @PostMapping("/upload")
    public String uploadAd(@RequestParam String name,
                           @RequestParam MultipartFile file,
                           @RequestParam Advertisement.Category category,
                           HttpSession session,
                           Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            adService.createAd(name, file, category, user);
            model.addAttribute("message", "广告上传成功");
        } catch (Exception e) {
            model.addAttribute("error", "上传失败: " + e.getMessage());
        }

        model.addAttribute("categories", Advertisement.Category.values());
        return "upload";
    }
}