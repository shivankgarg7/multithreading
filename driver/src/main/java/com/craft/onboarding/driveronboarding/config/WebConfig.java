package com.craft.onboarding.driveronboarding.config;

import com.craft.onboarding.driveronboarding.utils.JwtTokenInterceptor;
import com.craft.onboarding.driveronboarding.utils.RoleBasedAccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor;

    @Autowired
    private RoleBasedAccessInterceptor roleBasedAccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor).addPathPatterns("/auth/**");
        registry.addInterceptor(roleBasedAccessInterceptor).addPathPatterns("/admin/**");
    }
}