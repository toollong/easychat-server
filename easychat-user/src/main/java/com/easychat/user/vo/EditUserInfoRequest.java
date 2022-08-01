package com.easychat.user.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-04-20 12:47
 */
@Data
public class EditUserInfoRequest {

    private String userId;
    private String nickName;
    private Integer gender;
    private String birthday;
    private String region;
    private String email;
    private String phone;
    private String introduction;

}
