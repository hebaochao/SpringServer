package com.it.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @program: SpringServer
 * @description:
 * @author: baochao
 * @create: 2020-03-02 23:48
 **/
@SpringBootApplication
public class MainApplication {




    public static void main(String[] args) {
        new SpringApplication(MainApplication.class).run(args);
    }
}
