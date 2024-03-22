package com.optimagrowth.license.service.utils;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserContextInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        try {
            // Добавление заголовка корреляционного идентификатора
            String correlationId = UserContextHolder.getContext().getCorrelationId();
            if (correlationId != null) {
                template.header(UserContext.CORRELATION_ID, correlationId);
            }

            // Добавление заголовка токена аутентификации
            String authToken = UserContextHolder.getContext().getAuthToken();
            if (authToken != null) {
                template.header(UserContext.AUTH_TOKEN, authToken);
            }

        } catch (Exception e) {
            log.error("Ошибка при добавлении пользовательского контекста в Feign Request", e);
        }
    }
}
