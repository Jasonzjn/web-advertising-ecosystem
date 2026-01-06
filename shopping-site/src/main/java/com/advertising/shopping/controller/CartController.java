package com.advertising.shopping.controller;

import com.advertising.shopping.entity.Cart;
import com.advertising.shopping.service.CartService;
import com.advertising.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // 用于生成订单号的原子计数器
    private static final AtomicLong orderIdGenerator = new AtomicLong(System.currentTimeMillis() / 1000);

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Cart cart = cartService.getCart(session);
        model.addAttribute("cart", cart);
        model.addAttribute("websiteName", "购物车");
        return "cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        cartService.addToCart(productId, quantity, session);
        return cartService.getCartItemCount(session);
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateCart(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        cartService.updateCart(productId, quantity, session);
        Cart cart = cartService.getCart(session);
        return cart.getTotalPrice().toString();
    }

    @PostMapping("/remove")
    @ResponseBody
    public String removeFromCart(@RequestParam Long productId, HttpSession session) {
        cartService.removeFromCart(productId, session);
        return cartService.getCartItemCount(session);
    }

    @GetMapping("/count")
    @ResponseBody
    public String getCartCount(HttpSession session) {
        return cartService.getCartItemCount(session);
    }

    // 结算页面
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Cart cart = cartService.getCart(session);
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cart", cart);
        model.addAttribute("websiteName", "结算页面");
        return "checkout";
    }

    // 订单确认页面
    @GetMapping("/order-confirmation")
    public String orderConfirmation(HttpSession session, Model model) {
        Cart cart = cartService.getCart(session);

        // 生成订单号（使用时间戳+计数器的方式）
        long orderId = orderIdGenerator.getAndIncrement();
        String orderNumber = "ORD-" + orderId;

        model.addAttribute("orderNumber", orderNumber);
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("websiteName", "订单完成");
        return "order-confirmation";
    }

    // 处理结算
    @PostMapping("/process-checkout")
    public String processCheckout(HttpSession session, Model model) {
        Cart cart = cartService.getCart(session);
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        // 生成订单号
        String orderNumber = "ORD-" + System.currentTimeMillis();

        // 模拟支付处理
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("orderNumber", orderNumber);
        model.addAttribute("websiteName", "订单完成");

        // 清空购物车
        cartService.clearCart(session);

        return "order-confirmation";
    }

    // 接收带有订单数据的结算请求
    @PostMapping(value = "/process-checkout", consumes = "application/json")
    @ResponseBody
    public Map<String, Object> processCheckoutWithDetails(@RequestBody Map<String, String> orderDetails, HttpSession session, Model model) {
        Cart cart = cartService.getCart(session);
        if (cart.getItems().isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("redirect", "/cart/order-confirmation");
            return response;
        }

        // 生成订单号
        String orderNumber = "ORD-" + System.currentTimeMillis();

        // 在这里可以处理订单详细信息
        String recipientName = orderDetails.get("recipientName");
        String phone = orderDetails.get("phone");
        String address = orderDetails.get("address");
        String deliveryTime = orderDetails.get("deliveryTime");
        String paymentMethod = orderDetails.get("paymentMethod");

        // 模拟订单处理
        model.addAttribute("cart", cart);
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("orderNumber", orderNumber);
        model.addAttribute("websiteName", "订单完成");

        // 清空购物车
        cartService.clearCart(session);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("redirect", "/cart/order-confirmation");
        return response;
    }
}