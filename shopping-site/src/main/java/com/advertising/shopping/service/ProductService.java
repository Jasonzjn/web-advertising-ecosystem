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
                new BigDecimal("599.00"), "data:image/webp;base64,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", 50, ProductType.SPORT));

        productList.add(new Product(2L, "健身手环",
                "智能健身手环，可监测心率、步数、睡眠质量等。",
                new BigDecimal("299.00"), "https://th.bing.com/th/id/OIP.PnO1MSYiFUmPa9h2u13ihQHaHa?w=175&h=180&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3", 100, ProductType.SPORT));

        // 添加ELECTRONIC类型的商品
        productList.add(new Product(3L, "无线蓝牙耳机",
                "高音质无线蓝牙耳机，降噪功能强大，续航时间长。",
                new BigDecimal("799.00"), "https://th.bing.com/th/id/OIP.RVJZ2Nmvn24ILGPA7vAeTQHaHa?w=213&h=213&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3", 80, ProductType.ELECTRONIC));

        productList.add(new Product(4L, "智能手表",
                "多功能智能手表，支持健康监测、消息提醒等功能。",
                new BigDecimal("1299.00"), "https://th.bing.com/th/id/OIP.5uTT40WACpcrULyEnbJ84gHaHa?w=178&h=180&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3", 60, ProductType.ELECTRONIC));

        // 添加CLOTHING类型的商品
        productList.add(new Product(5L, "时尚休闲T恤",
                "纯棉材质，舒适透气，多种颜色可选。",
                new BigDecimal("89.00"), "https://th.bing.com/th/id/OIP.6Tp8CC2VY44jDpznQmS8YgHaHa?w=194&h=193&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3", 200, ProductType.CLOTHING));

        productList.add(new Product(6L, "商务正装",
                "高品质商务正装，适合正式场合穿着。",
                new BigDecimal("599.00"), "https://th.bing.com/th/id/OIP.37hxjRZLBJ9x0AnOYzpdAQHaLI?w=194&h=292&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3", 30, ProductType.CLOTHING));

        // 添加FOOD类型的商品
        productList.add(new Product(7L, "有机水果礼盒",
                "精选有机水果，新鲜健康，适合送礼或自用。",
                new BigDecimal("199.00"), "https://th.bing.com/th/id/OIP.ArKGns_AxcSJ4DfPruhH5gHaHa?w=203&h=203&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3", 40, ProductType.FOOD));

        productList.add(new Product(8L, "进口巧克力",
                "比利时进口巧克力，口感丝滑，包装精美。",
                new BigDecimal("129.00"), "https://th.bing.com/th/id/OIP.MxhplaLuuu45XMYIn1Jy9gHaHa?w=182&h=182&c=7&r=0&o=7&dpr=1.5&pid=1.7&rm=3", 75, ProductType.FOOD));
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