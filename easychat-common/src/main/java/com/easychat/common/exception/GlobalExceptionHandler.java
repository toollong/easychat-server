package com.easychat.common.exception;

import com.easychat.common.protocol.CommonCode;
import com.easychat.common.protocol.ResponseResult;
import com.easychat.common.protocol.ResultCode;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: long
 * @Date: 2022-04-19 10:33
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseResult<Object> exceptionHandle(Exception e) {
        LOGGER.error("catch exception : {}\r\nexception: ", e.getMessage(), e);
        if (e instanceof CustomException) {
            CustomException customException = (CustomException) e;
            ResultCode resultCode = customException.getResultCode();
            return ResponseResult.error(resultCode);
        }
        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();
        }
        final ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        if (resultCode != null) {
            return ResponseResult.error(resultCode);
        }
        return ResponseResult.error(CommonCode.UNKNOWN_ERROR);
    }

    static {
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }
}
