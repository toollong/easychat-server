package com.easychat.chat.config;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author: long
 * @Date: 2022-05-05 9:57
 */
@EnableConfigurationProperties(SocketIOProperties.class)
@Configuration
public class SocketIOConfig {

    @Resource
    SocketIOProperties properties;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setPort(properties.getPort());
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        config.setSocketConfig(socketConfig);
        config.setAllowCustomRequests(properties.getAllowCustomRequests());
        config.setUpgradeTimeout(properties.getUpgradeTimeout());
        config.setPingTimeout(properties.getPingTimeout());
        config.setPingInterval(properties.getPingInterval());
        config.setMaxHttpContentLength(properties.getMaxHttpContentLength());
        config.setMaxFramePayloadLength(properties.getMaxFramePayloadLength());
        config.setTransports(Transport.WEBSOCKET);
        return new SocketIOServer(config);
    }

    /**
     * 开启 SocketIOServer 注解支持
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner() {
        return new SpringAnnotationScanner(this.socketIOServer());
    }
}
