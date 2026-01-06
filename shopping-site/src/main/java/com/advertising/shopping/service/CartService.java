package com.advertising.shopping.service;

import com.advertising.shopping.entity.Cart;
import com.advertising.shopping.entity.CartItem;
import com.advertising.shopping.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

@Service
public class CartService {

    private static final String CART_SESSION_KEY = "cart";

    @Autowired
    private ProductService productService;

    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(Long productId, int quantity, HttpSession session) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            Cart cart = getCart(session);
            for (CartItem item : cart.getItems()) {
                if (item.getProduct().getId().equals(productId)) {
                    item.setQuantity(item.getQuantity() + quantity);
                    return;
                }
            }
            cart.getItems().add(new CartItem(product, quantity));
        }
    }

    public void updateCart(Long productId, int quantity, HttpSession session) {
        Cart cart = getCart(session);
        cart.updateQuantity(productId, quantity);
    }

    public void removeFromCart(Long productId, HttpSession session) {
        Cart cart = getCart(session);
        cart.removeItem(productId);
    }

    public String getCartItemCount(HttpSession session) {
        Cart cart = getCart(session);
        return String.valueOf(cart.getTotalItems());
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }
}