package com.akhmadreiza.arawebmvc.controller;

import com.akhmadreiza.arawebmvc.domain.Content;
import com.akhmadreiza.arawebmvc.domain.MetaTag;
import com.akhmadreiza.arawebmvc.service.ContentService;
import com.akhmadreiza.arawebmvc.service.MediaService;
import com.akhmadreiza.arawebmvc.util.MetaTagGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class BlogController {

    @Value("${araweb.wp.url}")
    private String wordpressUrl;

    @Value("${araweb.default.featured.image}")
    private String defaultImage;

    private ContentService contentService;

    private HomeController homeController;

    private MediaService mediaService;

    public BlogController(ContentService contentService, HomeController homeController, MediaService mediaService) {
        this.contentService = contentService;
        this.homeController = homeController;
        this.mediaService = mediaService;
    }

    @GetMapping("/blog")
    public String getBlogList(Model model) {
        List<Content> contentList = contentService.getContents(wordpressUrl + "/wp/v2/posts");
        model.addAttribute("blogPosts", contentList);
        return "blog";
    }

    @GetMapping("/{slug}")
    public String getBlogPost(Model model, @PathVariable(name = "slug") String slug, HttpServletRequest httpServletRequest) {
        if (ObjectUtils.isEmpty(slug)) {
            return homeController.getHome(model);
        }

        String url = wordpressUrl + "/wp/v2/posts?slug=" + slug;
        List<Content> contentList = contentService.getContents(url);
        if (!contentList.isEmpty()) {
            Content content = contentList.get(0);
            if (content.getFeaturedMedia() != null && !content.getFeaturedMedia().equals("0")) {
                content.setMedia(mediaService.getFeaturedImage(content.getFeaturedMedia()));
            }
            model.addAttribute("content", content);
            MetaTag metaTag = MetaTagGenerator.generate(content, httpServletRequest);
            metaTag.setImgUrl(ObjectUtils.isEmpty(metaTag.getImgUrl()) ? defaultImage : metaTag.getImgUrl());
            model.addAttribute("metaTag", metaTag);
            return "post";
        }

        return "not-found";
    }
}
