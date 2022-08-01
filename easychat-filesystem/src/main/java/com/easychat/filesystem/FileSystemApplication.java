package com.easychat.filesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: long
 * @Date: 2022-04-29 12:56
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.easychat"})
public class FileSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileSystemApplication.class, args);
    }
}
