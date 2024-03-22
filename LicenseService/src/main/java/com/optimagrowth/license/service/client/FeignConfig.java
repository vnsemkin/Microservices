package com.optimagrowth.license.service.client;

import com.optimagrowth.license.service.utils.UserContextInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
        @Bean
        public RequestInterceptor userContextInterceptor() {
            return new UserContextInterceptor();
        }
}
