package com.advertising.news.service;

import com.advertising.news.entity.News;
import com.advertising.news.entity.NewsType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsService {
    private List<News> newsList = new ArrayList<>();

    @PostConstruct
    public void initializeNews() {
        // 添加SPORT类型的新闻
        newsList.add(new News(1L, "足球世界杯决赛精彩纷呈",
                "昨晚的世界杯决赛中，两队上演了一场精彩绝伦的比赛，最终通过点球大战决出胜负。",
                "天涯沦落人", "2022-12-19 01:55", NewsType.SPORT,
                "https://th.bing.com/th/id/R.7dc35288b43cf5d7f870c7d11114eaaf?rik=DhnaSHpVRJXVpQ&riu=http%3a%2f%2foss.suning.com%2fsnsis%2fchannel_content%2fxKiUZgNbISFw0HRde5iC1pnqgj76XjavfyWTL9068naD0e2Jsj5Qjy7F-6f5H_5Q.jpg%3fimgW%3d1024%26imgH%3d683&ehk=JocZul3VtQEwBr28Q%2fmeT%2fkpwtPmxxBV4Tp1mWiufL4%3d&risl=&pid=ImgRaw&r=0"));

        newsList.add(new News(2L, "奥运会筹备工作进展顺利",
                "2024年奥运会各项筹备工作正在有序进行，场馆建设已基本完成。",
                "刘亮&刘珊","2024-07-22 09:55:53", NewsType.SPORT,""));

        // 添加ELECTRONIC类型的新闻
        newsList.add(new News(3L, "2025年上半年智能手机发布潮来袭",
                "2025年上半年，智能手机市场将迎来一波重磅新机发布潮。各大厂商纷纷推出自家的旗舰产品，力求在激烈的市场竞争中脱颖而出。今天，我们就来盘点一下即将在2025年上半年发布的热门手机型号，看看这些新机型有哪些亮点和创新。" +
                        "三星 Galaxy S25 系列——开启2025年的旗舰之战",
                "征服地球超人", "2025-02-04 22:49:00", NewsType.ELECTRONIC,
                ""));

        // 添加CLOTHING类型的新闻
        newsList.add(new News(4L, "时尚永续，我们未来穿什么？——中国国际时装周的可持续时尚 ",
                "本季度时尚周上，各大品牌展示了最新的服装设计，环保材料成为新趋势。",
                "罗欣桐 & Ann", "2024-05-06 16:38", NewsType.CLOTHING,
                "https://q5.itc.cn/images01/20240506/dd5e765718e449eda30d94a989a95ec0.gif"));

        // 添加FOOD类型的新闻
        newsList.add(new News(5L, "20万市民游客观展！三大“国字号”美食节在青岛引爆节会经济",
                "舌尖”盛宴引爆节会经济\n" +
                        "\n" +
                        "吸引20万市民游客观展\n" +
                        "\n" +
                        "三大“国字号”美食节在西海岸新区落幕",
                "青岛日报社/观海新闻", "2025-09-14 19:34", NewsType.FOOD,
                "http://news.qingdaonews.com/images/2025-09/14/fa45538f-f5b0-407f-bbb3-fd90fcf841e3.jpeg"));

        newsList.add(new News(6L, "2024食品营养健康趋势报告：怎么吃才健康？",
                "当下，\"新健康”已经逐渐成为国民食饮消费的新关键词。那怎么才算吃得健康？国民营养健康还面临哪些挑战呢？",
                "匿名", "2024-05-30 13:53:00", NewsType.FOOD,
                "https://picx.zhimg.com/70/v2-3e9331a5131e55b37c28a7d2f658421d_1440w.avis?source=172ae18b&biz_tag=Post"));
    }

    public List<News> getAllNews() {
        return new ArrayList<>(newsList);
    }

    public List<News> getNewsByType(NewsType type) {
        return newsList.stream()
                .filter(news -> news.getType() == type)
                .collect(Collectors.toList());
    }

    public News getNewsById(Long id) {
        return newsList.stream()
                .filter(news -> news.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}