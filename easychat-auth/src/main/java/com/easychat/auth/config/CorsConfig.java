package com.easychat.auth.config;

import org.springframework.context.annotation.Configuration;

/**
 * @Author: long
 * @Date: 2022-05-01 11:20
 */
@Configuration
public class CorsConfig {

    //    @Bean
    //    public CorsWebFilter corsWebFilter() {
    //        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //        CorsConfiguration config = new CorsConfiguration();
    //        config.addAllowedOrigin("*");
    //        config.addAllowedHeader("*");
    //        config.addAllowedMethod("*");
    //        config.setAllowCredentials(true);
    //        config.setMaxAge(3600L);
    //        source.registerCorsConfiguration("/**", config);
    //        return new CorsWebFilter(source);
    //    }
}
