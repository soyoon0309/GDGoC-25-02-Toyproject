package org.example.springbootdeveloper.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 웹 브라우저 URL '/img/**' 요청을 로컬 'uploads' 폴더로 매핑
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///" + System.getProperty("user.dir") + "/imgfiles/");
    }
}