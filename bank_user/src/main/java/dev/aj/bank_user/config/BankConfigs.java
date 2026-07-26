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
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.jsontype.PolymorphicTypeValidator;

import java.time.Duration;
import java.util.Locale;

@Configuration
public class BankConfigs {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .allowIfSubType("dev.aj.")
                .allowIfSubType("java.util.")
                .allowIfSubType("java.time.")
                .build();

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(MyCustomTtlFunction.INSTANCE)
                        .disableCachingNullValues()
                        .serializeValuesWith(RedisSerializationContext.SerializationPair
                                .fromSerializer(GenericJacksonJsonRedisSerializer.builder()
                                        .enableDefaultTyping(typeValidator)
                                        .typePropertyName("@class")
                                        .customize(mapper -> mapper.propertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy()))
                                        .build())))
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
                    .contains("management")
                    && value instanceof TokenResponse tokenResponse) {
                return Duration.ofSeconds(tokenResponse.expiresIn());
            }
            return Duration.ofSeconds(10);
        }
    }


}
