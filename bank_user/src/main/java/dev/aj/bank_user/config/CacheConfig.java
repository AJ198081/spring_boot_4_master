package dev.aj.bank_user.config;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import dev.aj.commons.types.TokenResponse;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.Objects;

@Configuration
@NullMarked
public class CacheConfig {

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {


        return RedisCacheManager.builder(
                        RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory)
                )
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(MyCustomTtlFunction.INSTANCE)
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
                        .disableCachingNullValues())
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
        public Duration getTimeToLive(@Nullable Object key, @Nullable Object value) {
            if (Objects.nonNull(key) && key instanceof String && key.toString().contains("getOAuthToken")
                    && value instanceof TokenResponse tokenResponse) {
                return Duration.ofSeconds(tokenResponse.expiresIn() - 20);
            }
            return Duration.ofMinutes(10);
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());

        return redisTemplate;
    }

    //    @Bean
    public CacheManager hazelcastCacheManager() {

        return new HazelcastCacheManager(Hazelcast.newHazelcastInstance());
    }

}
