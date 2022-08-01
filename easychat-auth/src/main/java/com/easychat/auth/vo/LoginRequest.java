package com.easychat.auth.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-04-28 10:00
 */
@Data
public class LoginRequest {

    private String username;
    private String password;
    private Boolean loginFree;

}
