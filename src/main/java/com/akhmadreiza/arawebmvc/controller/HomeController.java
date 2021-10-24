package com.akhmadreiza.arawebmvc.controller;

import com.akhmadreiza.arawebmvc.domain.Content;
import com.akhmadreiza.arawebmvc.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Value("${araweb.wp.page.skill.url}")
    private String skillUrl;

    @Value("${araweb.wp.page.wexp.url}")
    private String wexpUrl;

    @Value("${araweb.wp.page.works.url}")
    private String recentWorkUrl;

    @Autowired
    private ContentService contentService;

    @GetMapping("/home")
    public String getHome(Model model) {
        Content contentSkill = contentService.getSingleContent(skillUrl);
        Content contentWexperience = contentService.getSingleContent(wexpUrl);
        Content contentRecentWorks = contentService.getSingleContent(recentWorkUrl);

        model.addAttribute("contentSkill", contentSkill.getContent());
        model.addAttribute("contentWexperience", contentWexperience.getContent());
        model.addAttribute("contentRecentWorks", contentRecentWorks.getContent());

        return "home";
    }

    @GetMapping("")
    public String getHomeNoPath(Model model) {
        return getHome(model);
    }
}
