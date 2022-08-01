package com.easychat.user.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-07-01 7:54
 */
@Data
public class ValidatePasswordRequest {

    private String userId;
    private String password;

}
