package com.easychat.common.protocol;

/**
 * @Author: long
 * @Date: 2022-04-18 11:24
 */
public enum CommonCode implements ResultCode {

    //================================== common =====================================

    SUCCESS(true, 10000, "操作成功！"),
    FAIL(false, 11111, "操作失败！"),
    NOT_LOGIN(false, 10001, "此操作需要登录系统！"),
    UNAUTHORISED(false, 10002, "权限不足，无权操作！"),
    INVALID_PARAM(false, 10003, "非法参数！"),
    EMPTY_ERROR(false, 10004, "传入对象为空或缺少必要的参数！"),
    UNKNOWN_ERROR(false, 99999, "系统未知异常！"),

    //================================== user =======================================

    USER_NOT_EXISTS(false, 20001, "用户不存在！"),
    USER_NOT_FRIEND(false, 20002, "你还不是他（她）的好友！"),

    //================================== upload =======================================

    UPLOAD_IMAGE_FORMAT_ERROR(false, 30001, "上传图片只支持 JPG 或 PNG 格式！"),
    UPLOAD_IMAGE_SIZE_ERROR(false, 30002, "上传图片不能超过 2MB ！"),
    UPLOAD_FILE_SIZE_ERROR(false, 30003, "上传文件不能超过 100MB ！");

    boolean success;
    int code;
    String message;

    CommonCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
