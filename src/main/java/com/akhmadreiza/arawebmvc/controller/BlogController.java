package com.akhmadreiza.arawebmvc.controller;

import com.akhmadreiza.arawebmvc.domain.Content;
import com.akhmadreiza.arawebmvc.service.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BlogController {

    private ContentService contentService;

    public BlogController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/blog")
    public String getBlogList(Model model) {
        List<Content> contentList = contentService.getContents("https://akhmadreiza.com/wp-json/wp/v2/posts");
        model.addAttribute("blogPosts", contentList);
        return "blog";
    }
}
