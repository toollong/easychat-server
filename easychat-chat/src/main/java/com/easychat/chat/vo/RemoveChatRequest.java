package com.easychat.chat.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-04-28 10:11
 */
@Data
public class RemoveChatRequest {

    private String userId;
    private String sessionId;

}
