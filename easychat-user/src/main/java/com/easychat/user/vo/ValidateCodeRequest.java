package com.easychat.user.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-06-30 18:05
 */
@Data
public class ValidateCodeRequest {

    private String email;
    private String verifyCode;

}
