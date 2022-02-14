package com.akhmadreiza.arawebmvc.controller;

import com.akhmadreiza.arawebmvc.domain.Content;
import com.akhmadreiza.arawebmvc.domain.MetaTag;
import com.akhmadreiza.arawebmvc.domain.Tag;
import com.akhmadreiza.arawebmvc.service.ContentService;
import com.akhmadreiza.arawebmvc.service.MediaService;
import com.akhmadreiza.arawebmvc.service.TagService;
import com.akhmadreiza.arawebmvc.util.MetaTagGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class BlogController {

    @Value("${araweb.wp.url}")
    private String wordpressUrl;

    @Value("${araweb.default.featured.image}")
    private String defaultImage;

    private final ContentService contentService;

    private final HomeController homeController;

    private final MediaService mediaService;

    private final TagService tagService;

    public BlogController(ContentService contentService, HomeController homeController, MediaService mediaService, TagService tagService) {
        this.contentService = contentService;
        this.homeController = homeController;
        this.mediaService = mediaService;
        this.tagService = tagService;
    }

    @GetMapping("/blog")
    public String getBlogList(Model model, @RequestParam(required = false) String tags) {
        String url = wordpressUrl + "/wp/v2/posts";
        List<Content> contentList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(tags)) {
            List<Tag> tagList = tagService.findTags(tags);
            if (!tagList.isEmpty()) {
                url = url.concat("?tags=").concat(String.valueOf(tagList.get(0).getId()));
            } else {
                model.addAttribute("blogPosts", Collections.EMPTY_LIST);
                model.addAttribute("tagged", tags);
                return "blog";
            }
        }
        contentList = contentService.getContents(url);
        model.addAttribute("blogPosts", contentList);
        model.addAttribute("tagged", tags);
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
