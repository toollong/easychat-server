package com.easychat.chat.dto;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-07-03 9:16
 */
@Data
public class VerifyInfo {

    private String senderId;
    private String senderNickName;
    private String senderAvatar;
    private String receiverId;
    private String receiverNickName;
    private String receiverAvatar;
    private String applyReason;
    private String remark;
    private Integer status;
    private Integer hasRead;
    private String createTime;

}
