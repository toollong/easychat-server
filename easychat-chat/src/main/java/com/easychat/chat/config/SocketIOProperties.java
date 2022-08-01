package com.easychat.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: long
 * @Date: 2022-05-05 10:02
 */
@Data
@ConfigurationProperties(prefix = "socketio")
public class SocketIOProperties {

    private Integer port;

    private Integer workCount;

    private Boolean allowCustomRequests;

    private Integer upgradeTimeout;

    private Integer pingTimeout;

    private Integer pingInterval;

    private Integer maxFramePayloadLength;

    private Integer maxHttpContentLength;

}
