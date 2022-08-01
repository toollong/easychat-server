package com.easychat.chat.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-04-28 10:08
 */
@Data
public class AddChatRequest {

    private String userId;
    private String friendUserId;

}
