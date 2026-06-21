package dev.aj.order_service.config;

import dev.aj.order_service.model.exception.RetryableException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.retry.RetryPolicy;
import org.springframework.core.retry.RetryTemplate;

@Configuration
public class RetryableMechanism {

    @Bean
    public RetryTemplate retryTemplate() {

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(RetryPolicy.builder()
                .includes(RetryableException.class)
                .build());

        return retryTemplate;
    }

}
