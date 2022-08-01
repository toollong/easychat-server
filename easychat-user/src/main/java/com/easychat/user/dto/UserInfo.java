package com.easychat.user.dto;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-04-18 10:35
 */
@Data
public class UserInfo {

    private String id;
    private String username;
    private String nickName;
    private String avatar;
    private Integer gender;
    private Integer age;
    private String birthday;
    private String region;
    private String email;
    private String phone;
    private String introduction;
    private Integer status;
    private String tags;

}
