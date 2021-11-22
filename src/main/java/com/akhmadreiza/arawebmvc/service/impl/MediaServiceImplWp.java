package com.akhmadreiza.arawebmvc.service.impl;

import com.akhmadreiza.arawebmvc.domain.Media;
import com.akhmadreiza.arawebmvc.service.MediaService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MediaServiceImplWp implements MediaService {

    @Value("${araweb.wp.ip}")
    private String websiteIpAddress;

    @Value("${araweb.url}")
    private String websiteUrl;

    private static final String MEDIA_URI = "/wp-json/wp/v2/media/";

    private final RestTemplate restTemplate;

    public MediaServiceImplWp(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Media getFeaturedImage(String mediaId) {
        String mediaUrl = websiteIpAddress + MEDIA_URI + mediaId;
        ResponseEntity<JsonNode> wpJsonNodeRes = restTemplate.getForEntity(mediaUrl, JsonNode.class);
        Media media = new Media();
        if (wpJsonNodeRes.getBody() != null) {
            JsonNode resBody = wpJsonNodeRes.getBody();
            media.setId(mediaId);
            media.setMediaUrl(resBody.get("guid").get("rendered").asText());
            media.setMediaType(resBody.get("media_type").asText());
            media.setMediaCaption(resBody.get("caption").get("rendered").asText());
            return media;
        }
        return null;
    }
}
