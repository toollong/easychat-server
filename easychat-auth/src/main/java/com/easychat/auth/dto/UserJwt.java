package com.easychat.auth.dto;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-05-01 14:05
 */
@Data
public class UserJwt {

    private String id;
    private String username;
    private String nickName;
    private String avatar;
    private String jwt;

}
