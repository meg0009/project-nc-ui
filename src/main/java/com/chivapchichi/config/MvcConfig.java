package com.chivapchichi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin-panel/homepage").setViewName("admin-panel/adminhome");
        registry.addViewController("/main.js").setViewName("main.js");
        registry.addViewController("/select.js").setViewName("admin-panel/select.js");
        registry.addViewController("/style.css").setViewName("style.css");
    }
}
