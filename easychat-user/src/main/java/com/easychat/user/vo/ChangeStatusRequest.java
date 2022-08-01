package com.easychat.user.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-07-26 8:23
 */
@Data
public class ChangeStatusRequest {

    private String userId;
    private Integer status;

}
