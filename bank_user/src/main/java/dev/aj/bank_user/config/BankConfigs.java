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
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.Locale;

@Configuration
public class BankConfigs {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .propertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy())
                .build();
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        var json = RedisSerializationContext.SerializationPair
                .fromSerializer(new GenericJacksonJsonRedisSerializer(objectMapper));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(MyCustomTtlFunction.INSTANCE)
                        .disableCachingNullValues()
                        .serializeValuesWith(json))
                .transactionAware()
                .enableStatistics()
                .build();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    enum MyCustomTtlFunction implements RedisCacheWriter.TtlFunction {

        INSTANCE;

        @Override
        public @NonNull Duration getTimeToLive(@Nullable Object key, @Nullable Object value) {
            if (key instanceof String && key.toString().toLowerCase(Locale.ROOT)
                    .contains("auth")
                    && value instanceof TokenResponse tokenResponse) {
                return Duration.ofSeconds(tokenResponse.expiresIn());
            }
            return Duration.ofSeconds(20);
        }
    }


}
