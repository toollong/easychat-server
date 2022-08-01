package com.easychat.user.service;

import com.easychat.common.exception.CustomException;
import com.easychat.common.protocol.ResponseResult;
import com.easychat.user.dto.UserInfo;
import com.easychat.user.dto.ValidationInfo;
import com.easychat.user.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: long
 * @Date: 2022-04-18 11:08
 */
public interface UserService {

    /**
     * 验证用户名是否存在，若存在则返回用户 id 和邮箱
     *
     * @param request 验证请求
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<ValidationInfo> validateUserUsername(
            ValidateUsernameRequest request) throws CustomException;

    /**
     * 根据用户 id 验证其密码是否正确
     *
     * @param request 验证请求
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> validateUserPassword(ValidatePasswordRequest request) throws CustomException;

    /**
     * 注册用户
     *
     * @param request 请求信息
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> registerUser(RegisterRequest request) throws CustomException;

    /**
     * 根据用户名模糊查询用户列表
     *
     * @param username 用户名
     * @return 用户信息列表
     * @throws CustomException
     */
    ResponseResult<List<UserInfo>> queryUserList(String username) throws CustomException;

    /**
     * 根据用户 id 查询用户信息
     *
     * @param userId 用户 id
     * @return 用户信息
     * @throws CustomException
     */
    ResponseResult<UserInfo> queryUserInfo(String userId) throws CustomException;

    /**
     * 根据用户名和密码查询用户信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户信息
     * @throws CustomException
     */
    ResponseResult<UserInfo> queryUserInfo(String username, String password) throws CustomException;

    /**
     * 更新用户信息
     *
     * @param request 请求信息
     * @return 更新后的用户信息
     * @throws CustomException
     */
    ResponseResult<UserInfo> updateUserInfo(EditUserInfoRequest request) throws CustomException;

    /**
     * 更新用户头像
     *
     * @param userId 用户 id
     * @param image 头像图片
     * @return 图片路径
     * @throws CustomException
     */
    ResponseResult<String> updateAvatar(String userId, MultipartFile image) throws CustomException;

    /**
     * 更新用户状态
     *
     * @param request 请求信息
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> updateStatus(ChangeStatusRequest request) throws CustomException;

    /**
     * 修改用户密码
     *
     * @param request 请求信息
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> updatePassword(ChangePasswordRequest request) throws CustomException;

    /**
     * 用户添加标签
     *
     * @param request 请求信息
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> updateTagsAdd(UpdateTagsRequest request) throws CustomException;

    /**
     * 用户删除标签
     *
     * @param request 请求信息
     * @return 响应结果
     * @throws CustomException
     */
    ResponseResult<Object> updateTagsRemove(UpdateTagsRequest request) throws CustomException;
}
