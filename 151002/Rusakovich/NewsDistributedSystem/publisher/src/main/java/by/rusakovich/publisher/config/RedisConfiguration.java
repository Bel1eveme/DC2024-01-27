package by.rusakovich.publisher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfiguration {
    @Value("${spring.data.redis.host}")
    public String host;
    @Value("${spring.data.redis.port}")
    public int port;
    @Value("${spring.data.redis.timeout}")
    public int timeout;
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(){
        var configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        var clientConfiguration = LettuceClientConfiguration.builder().commandTimeout(Duration.ofMillis(timeout)).build();
        return new LettuceConnectionFactory(configuration, clientConfiguration);
    }

    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate(){
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();
        LettuceConnectionFactory lettuceConnectionFactory = lettuceConnectionFactory();
        lettuceConnectionFactory.afterPropertiesSet();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
