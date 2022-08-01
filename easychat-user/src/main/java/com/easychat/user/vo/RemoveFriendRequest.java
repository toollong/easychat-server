package com.easychat.user.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-04-28 10:05
 */
@Data
public class RemoveFriendRequest {

    private String userId;
    private String friendUserId;

}
