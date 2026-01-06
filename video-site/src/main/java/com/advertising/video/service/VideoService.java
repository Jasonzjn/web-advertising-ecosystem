package com.advertising.video.service;

import com.advertising.video.entity.Video;
import com.advertising.video.entity.VideoType;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {
    private List<Video> videoList = new ArrayList<>();

    @PostConstruct
    public void initializeVideos() {
        // 添加CLOTHING类型的视频 - 使用本地视频文件，使用在线图片作为占位符
        videoList.add(new Video(1L, "时尚穿搭指南", "学习如何搭配不同风格的服装", "", "https://tse2-mm.cn.bing.net/th/id/OIP-C.JwdyGeNWxK7cHCxnmcfIiAHaLH?w=194&h=291&c=7&r=0&o=7&dpr=1.2&pid=1.7&rm=3", VideoType.CLOTHING));
        videoList.add(new Video(2L, "春夏流行趋势", "了解最新的春夏服装潮流", "", "https://pic3.zhimg.com/v2-77574252ee5569994e2cbe6f443a52ae_b.jpg", VideoType.CLOTHING));

        // 添加ELECTRONIC类型的视频
        videoList.add(new Video(3L, "最新智能手机评测", "全面评测最新款智能手机", "", "https://tse3-mm.cn.bing.net/th/id/OIP-C.EElY7uSkVY6DqX8fn7xprwHaFX?w=202&h=180&c=7&r=0&o=7&dpr=1.2&pid=1.7&rm=3", VideoType.ELECTRONIC));
        videoList.add(new Video(4L, "电脑组装教程", "一步步教你如何组装电脑", "/videos/computer.mp4", "https://tse3-mm.cn.bing.net/th/id/OIP-C.axKgSFhtfOxJNqZCAba8iQHaFj?w=249&h=186&c=7&r=0&o=7&dpr=1.2&pid=1.7&rm=3", VideoType.ELECTRONIC));

        // 添加SPORT类型的视频
        videoList.add(new Video(5L, "健身训练教程", "专业教练指导的健身训练", "/videos/exercise.mp4", "https://tse4-mm.cn.bing.net/th/id/OIP-C.vq2IoiurJUM2XcuSVzF34gHaD6?w=322&h=180&c=7&r=0&o=7&dpr=1.2&pid=1.7&rm=3", VideoType.SPORT));
        videoList.add(new Video(6L, "足球技巧教学", "提高足球技巧的实用教程", "/videos/football.mp4", "https://puui.qpic.cn/vpic_cover/b3501xlhds8/b3501xlhds8_1675178222_hz.jpg/1280", VideoType.SPORT));

        // 添加FOOD类型的视频
        videoList.add(new Video(7L, "家常菜制作", "简单易学的家常菜制作方法", "/videos/dish.mp4", "https://materials.cdn.bcebos.com/images/32058463/0ba0f0b7b5a6823697d9e4acc2652171.jpeg", VideoType.FOOD));
        videoList.add(new Video(8L, "烘焙技巧", "专业烘焙师分享的烘焙技巧", "/videos/bake.mp4", "https://tse2-mm.cn.bing.net/th/id/OIP-C.YGe-qBAMCV-k_XEKoFVGvQHaE7?w=272&h=181&c=7&r=0&o=7&dpr=1.2&pid=1.7&rm=3", VideoType.FOOD));
    }

    public List<Video> getAllVideos() {
        return new ArrayList<>(videoList);
    }

    public List<Video> getVideosByType(VideoType type) {
        return videoList.stream()
                .filter(video -> video.getType() == type)
                .collect(Collectors.toList());
    }

    public Video getVideoById(Long id) {
        return videoList.stream()
                .filter(video -> video.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}