package com.optimagrowth.license.config;

import com.optimagrowth.license.model.OrganizationChangeModel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
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

    @Bean
    public Consumer<OrganizationChangeModel> input() {
        return org -> log.info("Received an {} event for organization id {}",
            org.getAction(), org.getOrganizationId());
    }

    @Bean
    public Consumer<OrganizationChangeModel> inboundOrgChanges() {
        return organization -> {
            log.info("Received a message of type {}", organization.getType());
            log.info("Received a message with an event {} from the organization service for the organization id {}",
                organization.getType(), organization.getOrganizationId());
        };
    }


    @Bean
    public Supplier<String> outboundOrgChanges() {
        return () -> "Outbound org";
    }
}
