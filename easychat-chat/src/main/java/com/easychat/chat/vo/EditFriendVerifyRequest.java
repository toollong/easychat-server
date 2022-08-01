package com.easychat.chat.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-07-03 18:15
 */
@Data
public class EditFriendVerifyRequest {

    private String senderId;
    private String receiverId;
    private Integer status;
    private Integer hasRead;

}
