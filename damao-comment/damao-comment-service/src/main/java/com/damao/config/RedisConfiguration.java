package com.damao.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate<?,?> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("创建注册redis模板(●'◡'●)");
        RedisTemplate<?,?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //redisTemplate.setKeySerializer(new StringRedisSerializer());
        //这里设置了新的序列化器，便于命令行阅读
        Jackson2JsonRedisSerializer<?> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY); // 识别所有字段 PropertyAccessor 识别任何字段 JsonAutoDetect
        // objectMapper.enableDefaultTyping();  // enableDefaultTyping 已过期
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        jsonRedisSerializer.setObjectMapper(objectMapper);

        // String 的 序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key 采用 String的序列化
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash 的key采用String的序列化
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // hash 的 value 采用 String 的序列化 TODO 修改了
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        // value 序列化方式采用 Jackson
        // redisTemplate.setValueSerializer(jsonRedisSerializer);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
