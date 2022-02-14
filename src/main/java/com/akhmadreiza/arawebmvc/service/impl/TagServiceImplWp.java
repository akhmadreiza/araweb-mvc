package com.akhmadreiza.arawebmvc.service.impl;

import com.akhmadreiza.arawebmvc.domain.Tag;
import com.akhmadreiza.arawebmvc.service.TagService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TagServiceImplWp implements TagService {

    @Value("${araweb.wp.url}")
    private String wordpressUrl;

    @Autowired
    private RestTemplate restTemplate;

    private static final String URI = "/wp/v2";

    private static final String TAGS_URI = "/wp/v2/tags";

    @Override
    public List<Tag> findTags(String slug) {
        String url = wordpressUrl + TAGS_URI;
        if (!ObjectUtils.isEmpty(slug)) {
            url = url.concat("?slug=").concat(slug);
        }
        ResponseEntity<JsonNode> wpJsonNodeRes = restTemplate.getForEntity(url, JsonNode.class);
        List<Tag> result = new ArrayList<>();
        if (wpJsonNodeRes.getBody() != null) {
            JsonNode bodyArr = wpJsonNodeRes.getBody();
            if (bodyArr.isArray()) {
                for (JsonNode body : bodyArr) {
                    String tagSlug = body.get("slug").asText();
                    int tagId = body.get("id").asInt();
                    log.info("tagSlug={}, tagId={}", tagSlug, tagId);
                    result.add(Tag.builder().id(tagId).slug(tagSlug).build());
                }
            }
        }
        return result;
    }

    @Override
    public Tag findTag(int id) {
        String url = wordpressUrl + TAGS_URI;
        if (!ObjectUtils.isEmpty(id)) {
            url = url + "/" + id;
        }
        ResponseEntity<JsonNode> wpJsonNodeRes = restTemplate.getForEntity(url, JsonNode.class);
        JsonNode jsonNode = wpJsonNodeRes.getBody();
        return Tag.builder().id(jsonNode.get("id").asInt()).slug(jsonNode.get("slug").asText()).build();
    }
}
