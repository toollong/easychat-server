package com.easychat.user.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-07-03 10:14
 */
@Data
public class AddFriendVerifyRequest {

    private String senderId;
    private String receiverId;
    private String applyReason;
    private String remark;
    private Integer status;
    private Integer hasRead;
    private String createTime;

}
