package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    
    @GetMapping("/")
    public String index() {
        // 重定向到静态资源中的index.html
        return "redirect:/static/index.html";
    }
    
    @GetMapping("/index")
    public String indexPage() {
        return "redirect:/static/index.html";
    }
}