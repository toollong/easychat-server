package com.easychat.common.exception;

import com.easychat.common.protocol.ResultCode;

/**
 * @Author: long
 * @Date: 2022-04-19 10:31
 */
public class CustomException extends RuntimeException {

    private final ResultCode resultCode;

    public CustomException(ResultCode resultCode) {
        super("错误代码：" + resultCode.code() + " 错误信息：" + resultCode.message());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return this.resultCode;
    }
}
