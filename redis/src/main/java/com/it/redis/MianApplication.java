package com.it.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @program: SpringServer
 * @description:
 * @author: baochao
 * @create: 2020-02-25 22:15
 **/
@SpringBootApplication
public class MianApplication {


    public static void main(String[] args) {
        new SpringApplication(MianApplication.class).run(args);
    }

}
