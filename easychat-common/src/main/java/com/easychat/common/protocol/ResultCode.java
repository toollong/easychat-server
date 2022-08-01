package com.easychat.common.protocol;

/**
 * @Author: long
 * @Date: 2022-04-18 11:18
 */
public interface ResultCode {

    /**
     * 操作是否成功，true成功，false失败
     */
    boolean success();

    /**
     * 操作代码
     */
    int code();

    /**
     * 提示信息
     */
    String message();
}
