package com.easychat.user.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-06-30 19:16
 */
@Data
public class SendCodeRequest {

    private String email;
    private Integer type;

}
