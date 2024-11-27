// src/main/java/com/deinprojekt/config/WebConfig.java
package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

	    @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/**")
	                        .allowedOrigins("http://localhost:3000") // Erlaube Anfragen nur von deinem Frontend
	                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Erlaube spezifische Methoden
	                        .allowedHeaders("*") // Erlaube alle Header
	                        .allowCredentials(true); // Erlaube Cookies/Authentifizierungsinformationen
	            }
	        };
	    }
	}

