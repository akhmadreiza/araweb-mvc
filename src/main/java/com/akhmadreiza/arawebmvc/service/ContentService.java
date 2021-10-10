package com.akhmadreiza.arawebmvc.service;

import com.akhmadreiza.arawebmvc.domain.Content;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContentService {
    Content getSingleContent(String url);

    List<Content> getContents(String url);
}
