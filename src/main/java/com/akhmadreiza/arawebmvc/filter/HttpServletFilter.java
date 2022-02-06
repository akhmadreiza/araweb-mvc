package com.akhmadreiza.arawebmvc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class HttpServletFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        appendHeader(res);
        log.debug("Logging Request  {} : {}", req.getMethod(), req.getRequestURI());
        filterChain.doFilter(req, res);
        log.debug("Logging Response :{}", res.getContentType());
    }

    private void appendHeader(HttpServletResponse httpServletResponse) {
        appendNoCacheHeader(httpServletResponse);
    }

    private void appendNoCacheHeader(HttpServletResponse httpServletResponse) {
        httpServletResponse.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate");
        httpServletResponse.addHeader(HttpHeaders.PRAGMA, "no-cache");
    }
}
