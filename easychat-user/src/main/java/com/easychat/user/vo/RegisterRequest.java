package com.easychat.user.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-05-03 9:31
 */
@Data
public class RegisterRequest {

    private String username;
    private String nickName;
    private String password;
    private String email;
    private String verifyCode;
    private Boolean isAgree;

}
