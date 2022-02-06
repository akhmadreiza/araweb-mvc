package com.akhmadreiza.arawebmvc.config;

import com.akhmadreiza.arawebmvc.filter.HttpServletFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterRegistrationConfiguration {
    @Bean
    public FilterRegistrationBean<HttpServletFilter> httpServletFilter() {
        FilterRegistrationBean<HttpServletFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new HttpServletFilter());
        filterFilterRegistrationBean.addUrlPatterns("/*");
        filterFilterRegistrationBean.setOrder(1);
        return filterFilterRegistrationBean;
    }
}
