package com.easychat.common.protocol;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: long
 * @Date: 2022-04-18 11:13
 */
@Data
@NoArgsConstructor
public class ResponseResult<T> {

    boolean success = true;
    int code = 10000;
    String message;
    T data;

    public ResponseResult(ResultCode resultCode) {
        this.success = resultCode.success();
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public ResponseResult(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public ResponseResult(ResultCode resultCode, T data) {
        this.success = resultCode.success();
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.data = data;
    }

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(CommonCode.SUCCESS);
    }

    public static <T> ResponseResult<T> success(T data) {
        return new ResponseResult<>(CommonCode.SUCCESS, data);
    }

    public static <T> ResponseResult<T> error(ResultCode resultCode) {
        return new ResponseResult<>(resultCode);
    }

    public static <T> ResponseResult<T> fail() {
        return new ResponseResult<>(CommonCode.FAIL);
    }
}
