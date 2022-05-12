package com.example.walkddog_server.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/dogimg/**")
                .addResourceLocations(Constants.fileSystemPath);
        registry.addResourceHandler("/**")
                .addResourceLocations(Constants.staticPath);
    }
}
