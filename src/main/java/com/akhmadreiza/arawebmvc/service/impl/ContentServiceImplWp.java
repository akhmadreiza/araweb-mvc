package com.akhmadreiza.arawebmvc.service.impl;

import com.akhmadreiza.arawebmvc.domain.Content;
import com.akhmadreiza.arawebmvc.domain.Tag;
import com.akhmadreiza.arawebmvc.service.ContentService;
import com.akhmadreiza.arawebmvc.service.TagService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContentServiceImplWp implements ContentService {

    @Value("${araweb.wp.ip}")
    private String websiteIpAddress;

    @Value("${araweb.url}")
    private String websiteUrl;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    private final RestTemplate restTemplate;

    private final TagService tagService;

    public ContentServiceImplWp(RestTemplate restTemplate, TagService tagService) {
        this.restTemplate = restTemplate;
        this.tagService = tagService;
    }

    @Override
    public Content getSingleContent(String url) {
        ResponseEntity<JsonNode> wpJsonNodeRes = restTemplate.getForEntity(url, JsonNode.class);
        Content content = new Content();
        if (wpJsonNodeRes.getBody() != null) {
            JsonNode body = wpJsonNodeRes.getBody();
            content.setId(body.get("id").asText());
            content.setType(body.get("type").asText());
            content.setContent(body.get("content").get("rendered").asText());
            content.setTitle(body.get("title").get("rendered").asText());
        }
        return content;
    }

    @Override
    public List<Content> getContents(String url) {
        ResponseEntity<JsonNode> wpJsonNodeRes = restTemplate.getForEntity(url, JsonNode.class);
        List<Content> result = new ArrayList<>();
        if (wpJsonNodeRes.getBody() != null) {
            JsonNode bodyArr = wpJsonNodeRes.getBody();
            if (bodyArr.isArray()) {
                for (JsonNode body : bodyArr) {
                    Content content = new Content();
                    content.setId(body.get("id").asText());
                    content.setType(body.get("type").asText());
                    content.setContent(body.get("content").get("rendered").asText().replace(websiteIpAddress, websiteUrl));
                    content.setTitle(body.get("title").get("rendered").asText());
                    LocalDateTime dateTime = LocalDateTime.parse(body.get("date").asText(), ISO_FORMATTER);
                    content.setCreatedDate(dateTime.format(FORMATTER));
                    content.setContentShort(body.get("excerpt").get("rendered").asText().length() > 250 ? body.get("excerpt").get("rendered").asText().substring(0, 250) : body.get("excerpt").get("rendered").asText());
                    content.setSlug(body.get("slug").asText());
                    content.setFeaturedMedia(body.get("featured_media") != null ? body.get("featured_media").asText() : null);
                    content.setTags(fetchTags(body.get("tags")));
                    result.add(content);
                }
            }
        }
        return result;
    }

    private List<Tag> fetchTags(JsonNode bodyArr) {
        List<Tag> result = new ArrayList<>();
        if (bodyArr.isArray()) {
            for (JsonNode body : bodyArr) {
                Tag tag = tagService.findTag(body.asInt());
                result.add(tag);
            }
        }
        return result;
    }
}
