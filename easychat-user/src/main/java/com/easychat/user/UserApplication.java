package com.easychat.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: long
 * @Date: 2022-06-30 17:04
 */
@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.easychat"})
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
