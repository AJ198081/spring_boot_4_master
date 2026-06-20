package dev.aj.order_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.services.url")
public record ExternalServiceProperties(
       String billing,
        String customer,
        String payment,
        String product,
        String shipping
) {
}
