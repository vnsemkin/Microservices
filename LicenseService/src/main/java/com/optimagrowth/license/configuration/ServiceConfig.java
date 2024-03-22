package com.optimagrowth.license.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "example")
public class ServiceConfig {
    private String property;
    @Bean
    public LocaleResolver localeResolver() {
        var localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }
    @Bean
    public ResourceBundleMessageSource messageSource() {
        var messageSource =
                new ResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setBasenames("messages");
        return messageSource;
    }
}
