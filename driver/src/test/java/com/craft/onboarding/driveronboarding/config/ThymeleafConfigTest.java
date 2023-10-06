package com.craft.onboarding.driveronboarding.config;

import org.junit.jupiter.api.Test;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

class ThymeleafConfigTest {

    @Test
    void templateResolver_ShouldReturnConfiguredTemplateResolver() {
        // Arrange
        ThymeleafConfig thymeleafConfig = new ThymeleafConfig();
        ClassLoaderTemplateResolver templateResolver = thymeleafConfig.templateResolver();

        // Assert
        assert templateResolver != null;
        assert templateResolver.getPrefix().equals("/templates/");
        assert templateResolver.getSuffix().equals(".html");
        assert templateResolver.getTemplateMode() == TemplateMode.HTML;
        assert templateResolver.isCacheable();
    }

    @Test
    void templateEngine_ShouldReturnConfiguredTemplateEngine() {
        // Arrange
        ThymeleafConfig thymeleafConfig = new ThymeleafConfig();
        SpringTemplateEngine templateEngine = thymeleafConfig.templateEngine();

        // Assert
        assert templateEngine != null;

    }

    @Test
    void viewResolver_ShouldReturnConfiguredViewResolver() {
        // Arrange
        ThymeleafConfig thymeleafConfig = new ThymeleafConfig();
        ThymeleafViewResolver viewResolver = thymeleafConfig.viewResolver();

        // Assert
        assert viewResolver != null;
        assert viewResolver.getTemplateEngine() != null;
        assert viewResolver.getOrder() == 1;
        assert viewResolver.getViewNames() != null;
    }
}
