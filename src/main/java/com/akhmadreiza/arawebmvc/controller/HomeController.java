package com.akhmadreiza.arawebmvc.controller;

import com.akhmadreiza.arawebmvc.domain.Content;
import com.akhmadreiza.arawebmvc.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final String SKILL_URL = "https://akhmadreiza.com/wp-json/wp/v2/pages/70";

    private static final String WEXPERIENCE_URL = "https://akhmadreiza.com/wp-json/wp/v2/pages/62";

    private static final String RECENT_WORKS_URL = "https://akhmadreiza.com/wp-json/wp/v2/pages/77";

    @Autowired
    private ContentService contentService;

    @GetMapping("/home")
    public String getHome(Model model) {
        Content contentSkill = contentService.getSingleContent(SKILL_URL);
        Content contentWexperience = contentService.getSingleContent(WEXPERIENCE_URL);
        Content contentRecentWorks = contentService.getSingleContent(RECENT_WORKS_URL);

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
