package com.fake.information.sever.demo.DAO.redis

import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import javax.crypto.KeyGenerator

@Configuration
@EnableCaching
class RedisConfig : CachingConfigurerSupport() {
    @Bean
    fun redisTemplate(factory: RedisConnectionFactory): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        val redisSerializer = JdkSerializationRedisSerializer()
        redisTemplate.setConnectionFactory(factory)
        //key序列
        redisTemplate.keySerializer = redisSerializer
        //value序列化
        redisTemplate.valueSerializer = redisSerializer
        //value hashmap序列化
        redisTemplate.hashKeySerializer = redisSerializer
        //key hashmap序列化
        redisTemplate.hashValueSerializer = redisSerializer
        return redisTemplate
    }
}