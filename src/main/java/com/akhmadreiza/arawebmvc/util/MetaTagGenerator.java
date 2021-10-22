package com.akhmadreiza.arawebmvc.util;

import com.akhmadreiza.arawebmvc.domain.Content;
import com.akhmadreiza.arawebmvc.domain.MetaTag;
import org.jsoup.Jsoup;

import javax.servlet.http.HttpServletRequest;

public class MetaTagGenerator {

    private static final String TITLE_PREFIX = "Akhmad Reiza Armando | Blog | ";

    public static MetaTag generate(Content content, HttpServletRequest httpServletRequest) {
        MetaTag metaTag = new MetaTag();
        metaTag.setTitle(TITLE_PREFIX.concat(content.getTitle()));
        metaTag.setDescription(Jsoup.parse(content.getContentShort()).text());
        metaTag.setUrl(getFullURL(httpServletRequest));
        return metaTag;
    }

    private static String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
}
