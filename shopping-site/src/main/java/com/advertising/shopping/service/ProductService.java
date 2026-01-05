package com.advertising.shopping.service;

import com.advertising.shopping.entity.Product;
import com.advertising.shopping.entity.ProductType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private List<Product> productList = new ArrayList<>();

    @PostConstruct
    public void initializeProducts() {
        // 添加SPORT类型的商品
        productList.add(new Product(1L, "专业跑步鞋",
                "适合长跑的专业跑步鞋，具有良好的缓震效果和透气性。",
                new BigDecimal("599.00"), "https://via.placeholder.com/200", 50, ProductType.SPORT));

        productList.add(new Product(2L, "健身手环",
                "智能健身手环，可监测心率、步数、睡眠质量等。",
                new BigDecimal("299.00"), "https://via.placeholder.com/200", 100, ProductType.SPORT));

        // 添加ELECTRONIC类型的商品
        productList.add(new Product(3L, "无线蓝牙耳机",
                "高音质无线蓝牙耳机，降噪功能强大，续航时间长。",
                new BigDecimal("799.00"), "https://via.placeholder.com/200", 80, ProductType.ELECTRONIC));

        productList.add(new Product(4L, "智能手表",
                "多功能智能手表，支持健康监测、消息提醒等功能。",
                new BigDecimal("1299.00"), "https://via.placeholder.com/200", 60, ProductType.ELECTRONIC));

        // 添加CLOTHING类型的商品
        productList.add(new Product(5L, "时尚休闲T恤",
                "纯棉材质，舒适透气，多种颜色可选。",
                new BigDecimal("89.00"), "https://via.placeholder.com/200", 200, ProductType.CLOTHING));

        productList.add(new Product(6L, "商务正装",
                "高品质商务正装，适合正式场合穿着。",
                new BigDecimal("599.00"), "https://via.placeholder.com/200", 30, ProductType.CLOTHING));

        // 添加FOOD类型的商品
        productList.add(new Product(7L, "有机水果礼盒",
                "精选有机水果，新鲜健康，适合送礼或自用。",
                new BigDecimal("199.00"), "https://via.placeholder.com/200", 40, ProductType.FOOD));

        productList.add(new Product(8L, "进口巧克力",
                "比利时进口巧克力，口感丝滑，包装精美。",
                new BigDecimal("129.00"), "https://via.placeholder.com/200", 75, ProductType.FOOD));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(productList);
    }

    public List<Product> getProductsByType(ProductType type) {
        return productList.stream()
                .filter(product -> product.getType() == type)
                .collect(Collectors.toList());
    }

    public Product getProductById(Long id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}