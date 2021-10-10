package com.akhmadreiza.arawebmvc.service.impl;

import com.akhmadreiza.arawebmvc.domain.Content;
import com.akhmadreiza.arawebmvc.service.ContentService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ContentServiceImplWp implements ContentService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Content getSingleContent(String url) {
        ResponseEntity<JsonNode> wpJsonNodeRes = restTemplate.getForEntity(url, JsonNode.class);
        Content content = new Content();
        if (wpJsonNodeRes.getBody() != null) {
            JsonNode body = wpJsonNodeRes.getBody();
            content.setId(body.get("id").asText());
            content.setType(body.get("type").asText());
            content.setContent(body.get("content").get("rendered").asText());
        }
        return content;
    }

    @Override
    public List<Content> getContents(String url) {
        return null;
    }
}
