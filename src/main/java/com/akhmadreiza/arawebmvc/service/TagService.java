package com.akhmadreiza.arawebmvc.service;

import com.akhmadreiza.arawebmvc.domain.Tag;

import java.util.List;

public interface TagService {
    List<Tag> findTags(String slug);

    Tag findTag(int id);
}
