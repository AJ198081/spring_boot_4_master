package dev.aj.bank_user.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(value = {
        Auth0ManagementProperties.class
})
@EnableCaching
@Configuration
public class ComposeConfigurations {
}
