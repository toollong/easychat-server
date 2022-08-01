package com.easychat.chat;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author: long
 * @Date: 2022-04-18 9:59
 */
@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.easychat"})
@Slf4j
public class ChatApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        socketIOServer.start();
        log.info("Socket.IO已启动...");
    }
}
