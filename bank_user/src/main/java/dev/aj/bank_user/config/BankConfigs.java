package dev.aj.bank_user.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

@EnableConfigurationProperties(value = {
        Auth0ManagementProperties.class
})
@Configuration
public class BankConfigs {

    @Bean
    public JacksonJsonHttpMessageConverter jacksonHttpMessageConverter(JsonMapper.Builder builder) {

        return new JacksonJsonHttpMessageConverter(builder.build());
    }

    @Bean
    public JsonMapper.Builder jacksonBuilder() {

        return JsonMapper.builder()
                .propertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
    }

}
