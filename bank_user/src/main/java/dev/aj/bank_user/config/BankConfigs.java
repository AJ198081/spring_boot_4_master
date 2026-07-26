package dev.aj.bank_user.config;

import dev.aj.commons.types.TokenResponse;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

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

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(MyCustomTtlFunction.INSTANCE)
                        .disableCachingNullValues())
                .transactionAware()
                .enableStatistics()
                .build();
    }

    enum MyCustomTtlFunction implements RedisCacheWriter.TtlFunction {

        INSTANCE;

        @Override
        public @NonNull Duration getTimeToLive(@Nullable Object key, @Nullable Object value) {
            if (key instanceof String && key.toString().contains("getOAuthToken")
                    && value instanceof TokenResponse tokenResponse) {
                return Duration.ofSeconds(tokenResponse.expiresIn());
            }
            return Duration.ofSeconds(20);
        }
    }


}
