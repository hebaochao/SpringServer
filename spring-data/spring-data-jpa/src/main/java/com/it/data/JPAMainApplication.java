package com.it.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/***
 * springboot jpa main
 *
 */
@EnableJpaRepositories(basePackages = "com.it.data.dao")
@SpringBootApplication
public class JPAMainApplication {
    public static void main(String[] args) {
        new SpringApplication(JPAMainApplication.class).run(args);
    }
}
