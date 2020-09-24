package com.it.rediss;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component("mainApplication")
@SpringBootApplication
public class MainApplication {

    @Autowired
    private RedisTemplate redisTemplate;


    public void  test(){
        for (int i =0 ; i<10 ;i++){
            this.redisTemplate.opsForList().rightPush("test1","a"+i);
            this.redisTemplate.opsForList().rightPush("test2","b"+i);
        }

        for (int i = 0;i<10;i++){
            System.out.println("test1 :" +this.redisTemplate.opsForList().rightPop("test1"));
        }
        System.out.println("--------------------------------------------");
        for (int i = 0;i<10;i++){
            System.out.println("test2 :" +this.redisTemplate.opsForList().leftPop("test2"));
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context =   new SpringApplication(MainApplication.class).run(args);
        MainApplication mainApplication =   context.getBean("mainApplication",MainApplication.class);
        mainApplication.test();

    }



}
