package com.kindminds.drs.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

@Configuration
public class TilesViewResolverConfig {

    @Bean(name = "viewResolver")
    public ViewResolver getViewResolver() {
        TilesViewResolver viewResolver = new TilesViewResolver();

        //viewResolver.setCache(true);
        viewResolver.setViewClass(org.springframework.web.servlet.view.tiles3.TilesView.class);
        viewResolver.setOrder(1);

        return viewResolver;
    }

    @Bean(name = "tilesConfigurer")
    public TilesConfigurer getTilesConfigurer() {
        TilesConfigurer config = new TilesConfigurer();
        
        config.setDefinitions("/WEB-INF/tiles.xml");

        return config;
    }

}