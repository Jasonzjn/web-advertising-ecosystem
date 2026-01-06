package org.example.adplatform.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.adplatform.model.Advertisement;
import org.example.adplatform.service.AdService;
import org.example.adplatform.service.CrossDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")  // 全局跨域配置
public class ApiController {

    @Autowired
    private AdService adService;

    @Autowired
    private CrossDomainService crossDomainService;

    @GetMapping("/get-ad")
    public ResponseEntity<?> getAdByPreference(@RequestParam Map<String,Integer> preference,
                                               @RequestParam String fileType){
        Advertisement response = adService.getAdByPreference(preference,fileType);
        return ResponseEntity.ok(response);
    }

}