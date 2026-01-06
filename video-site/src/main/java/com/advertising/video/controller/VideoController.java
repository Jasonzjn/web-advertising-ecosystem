package com.advertising.video.controller;

import com.advertising.video.entity.Video;
import com.advertising.video.entity.VideoType;
import com.advertising.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public String index(Model model) {
        List<Video> videos = videoService.getAllVideos();
        model.addAttribute("videos", videos);
        model.addAttribute("websiteName", "视频网站");
        return "index";
    }

    @GetMapping("/type/{type}")
    public String videosByType(@PathVariable String type, Model model) {
        VideoType videoType = VideoType.valueOf(type.toUpperCase());
        List<Video> videos = videoService.getVideosByType(videoType);
        model.addAttribute("videos", videos);
        model.addAttribute("websiteName", videoType.name() + "视频");
        return "index";
    }

    // 修改映射模式，只匹配数字ID
    @GetMapping("/{id:\\d+}")
    public String videoDetail(@PathVariable Long id, Model model) {
        Video video = videoService.getVideoById(id);
        model.addAttribute("video", video);
        model.addAttribute("websiteName", video.getTitle());
        return "video-detail";
    }
}