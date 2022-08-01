package com.easychat.user.vo;

import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-07-26 8:45
 */
@Data
public class UpdateTagsRequest {

    private String userId;
    private String tag;

}
