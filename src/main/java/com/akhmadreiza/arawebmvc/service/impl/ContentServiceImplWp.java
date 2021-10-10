package com.akhmadreiza.arawebmvc.service.impl;

import com.akhmadreiza.arawebmvc.domain.Content;
import com.akhmadreiza.arawebmvc.service.ContentService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContentServiceImplWp implements ContentService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    private final RestTemplate restTemplate;

    public ContentServiceImplWp(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
                    content.setContent(body.get("content").get("rendered").asText().replace("http://206.189.86.170:8000", "https://akhmadreiza.com"));
                    content.setTitle(body.get("title").get("rendered").asText());
                    LocalDateTime dateTime = LocalDateTime.parse(body.get("date").asText(), ISO_FORMATTER);
                    content.setCreatedDate(dateTime.format(FORMATTER));
                    content.setContentShort(body.get("excerpt").get("rendered").asText().length() > 300 ? body.get("excerpt").get("rendered").asText().substring(0, 300) : body.get("excerpt").get("rendered").asText());
                    content.setSlug(body.get("slug").asText());
                    result.add(content);
                }
            }
        }
        return result;
    }
}
