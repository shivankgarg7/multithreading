package com.craft.onboarding.driveronboarding.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Configuration
@ConfigurationProperties("documents")
@PropertySource(value = "classpath:document-config.yaml", factory = YamlPropertySourceFactory.class)
public class DocumentConfig {

    private Map<String, Map<String, List<String>>> documentMapPerCity = new HashMap<>();

    public Map<String, Map<String, List<String>>> getDocumentMapPerCity() {
        return documentMapPerCity;
    }

    public void setDocumentMapPerCity(Map<String, Map<String, List<String>>> documentMapPerCity) {
        this.documentMapPerCity = documentMapPerCity;
    }
}