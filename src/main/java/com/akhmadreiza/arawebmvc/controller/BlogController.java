package com.akhmadreiza.arawebmvc.controller;

import com.akhmadreiza.arawebmvc.domain.Content;
import com.akhmadreiza.arawebmvc.service.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BlogController {

    private static final String BASE_URL = "https://akhmadreiza.com/wp-json";

    private ContentService contentService;

    private HomeController homeController;

    public BlogController(ContentService contentService, HomeController homeController) {
        this.contentService = contentService;
        this.homeController = homeController;
    }

    @GetMapping("/blog")
    public String getBlogList(Model model) {
        List<Content> contentList = contentService.getContents(BASE_URL +"/wp/v2/posts");
        model.addAttribute("blogPosts", contentList);
        return "blog";
    }

    @GetMapping("/{slug}")
    public String getBlogPost(Model model, @PathVariable(name = "slug") String slug) {
        if (ObjectUtils.isEmpty(slug)) {
            return homeController.getHome(model);
        }

        String url = BASE_URL + "/wp/v2/posts?slug=" + slug;
        List<Content> contentList = contentService.getContents(url);
        if (!contentList.isEmpty()) {
            Content content = contentList.get(0);
            model.addAttribute("content", content);
            return "post";
        }

        return "not-found";
    }
}
