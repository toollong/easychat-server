package com.easychat.user.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-05-03 10:00
 */
@Data
public class ChangePasswordRequest {

    private String userId;
    private String newPassword;
    private String checkPassword;

}
