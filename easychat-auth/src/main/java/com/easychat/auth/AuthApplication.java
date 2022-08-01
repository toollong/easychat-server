package com.easychat.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: long
 * @Date: 2022-04-30 12:51
 */
@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.easychat"})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
