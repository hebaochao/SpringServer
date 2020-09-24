package com.it.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserMainApplication {

    public static void main(String[] args) {
        new SpringApplication(UserMainApplication.class).run(args);
    }

}
