package com.it.rediss.bean;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

@Configurable
public class RedissBeanConfig {

     @Bean
    public RedisTemplate<String,String> redisTemplate (){
        return  new RedisTemplate<String, String>();
    }
}
