package com.simm.common.interceptor;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import java.util.List;

/**
 * 跨域统一拦截
 * @author simm
 */
//@Configuration
@Data
public class CrosInterceptor implements WebFluxConfigurer {
    @Value("#{'${cors.allowedOrigins:*}'.split(',')}")
    private List<String> allowedOrigins;
    @Value("#{'${cors.allowedHeaders:*}'.split(',')}")
    private List<String> allowedHeaders;
    @Value("#{'${cors.allowedMethods:*}'.split(',')}")
    private List<String> allowedMethods;
    @Value("${cors.corsMapping:/**}")
    private String corsMapping;
    /**
     * corsWebFilter需要早于AuthFiler
     */
//    @Bean
//    @Order(-200) //非常重要，一定要早于AuthFilter
    CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        for(String val:allowedOrigins){
            config.addAllowedOrigin(val);
        }
        for(String val:allowedHeaders){
            config.addAllowedHeader(val);
        }
        for(String val:allowedMethods){
            config.addAllowedMethod(val);
        }
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsMapping, config);
        return new CorsWebFilter(source);
    }
}
