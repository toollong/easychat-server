package com.easychat.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.EncryptUtils;
import com.easychat.common.exception.CustomException;
import com.easychat.common.protocol.CommonCode;
import com.easychat.common.protocol.ResponseResult;
import com.easychat.user.dto.UserInfo;
import com.easychat.user.dto.ValidationInfo;
import com.easychat.user.entity.User;
import com.easychat.user.feign.FileFeignService;
import com.easychat.user.mapper.UserMapper;
import com.easychat.user.service.UserService;
import com.easychat.user.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author: long
 * @Date: 2022-04-18 11:08
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    FileFeignService fileFeignService;

    @Override
    public ResponseResult<ValidationInfo> validateUserUsername(ValidateUsernameRequest request)
            throws CustomException {
        if (request == null || StringUtils.isBlank(request.getUsername())) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        if (request.getUsername().length() < 6 || request.getUsername().length() > 11) {
            return ResponseResult.fail();
        }
        User user = userMapper
                .selectOne(new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, request.getUsername()));
        if (user != null) {
            ValidationInfo validationInfo = new ValidationInfo();
            validationInfo.setUserId(user.getId());
            validationInfo.setEmail(user.getEmail());
            return ResponseResult.success(validationInfo);
        }
        return ResponseResult.fail();
    }

    @Override
    public ResponseResult<Object> validateUserPassword(ValidatePasswordRequest request)
            throws CustomException {
        if (request == null
                || StringUtils.isBlank(request.getUserId())
                || StringUtils.isBlank(request.getPassword())) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        if (request.getPassword().length() < 8 || request.getPassword().length() > 16) {
            return ResponseResult.fail();
        }
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getId, request.getUserId())
                .eq(User::getPassword, EncryptUtils.md5Base64(request.getPassword())));
        if (count > 0) {
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Object> registerUser(RegisterRequest request) throws CustomException {
        if (request == null
                || StringUtils.isBlank(request.getUsername())
                || StringUtils.isBlank(request.getPassword())
                || StringUtils.isBlank(request.getNickName())
                || StringUtils.isBlank(request.getEmail())
                || StringUtils.isBlank(request.getVerifyCode())
                || !request.getIsAgree()) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        if (request.getUsername().length() < 6 || request.getUsername().length() > 11
                || request.getPassword().length() < 8 || request.getPassword().length() > 16
                || request.getNickName().length() > 11) {
            throw new CustomException(CommonCode.INVALID_PARAM);
        }
        Long count = userMapper
                .selectCount(new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, request.getUsername()));
        if (count < 1) {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(EncryptUtils.md5Base64(request.getPassword()));
            user.setNickName(request.getNickName());
            user.setEmail(request.getEmail());
            user.setCreateTime(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            userMapper.insert(user);
            log.info("用户注册成功：{}", user);
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    @Override
    public ResponseResult<List<UserInfo>> queryUserList(String username) throws CustomException {
        if (StringUtils.isBlank(username)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        List<UserInfo> userInfoList = userMapper.selectUserList(username);
        return ResponseResult.success(userInfoList);
    }

    @Override
    public ResponseResult<UserInfo> queryUserInfo(String userId) throws CustomException {
        if (StringUtils.isBlank(userId)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseResult.fail();
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo);
        return ResponseResult.success(userInfo);
    }

    @Override
    public ResponseResult<UserInfo> queryUserInfo(String username, String password) throws CustomException {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        if (username.length() < 6 || username.length() > 11
                || password.length() < 8 || password.length() > 16) {
            return ResponseResult.fail();
        }
        User user = userMapper
                .selectOne(new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, username)
                        .eq(User::getPassword, EncryptUtils.md5Base64(password)));
        if (user != null) {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(user, userInfo);
            return ResponseResult.success(userInfo);
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<UserInfo> updateUserInfo(EditUserInfoRequest request) throws CustomException {
        if (request == null
                || StringUtils.isBlank(request.getUserId())
                || StringUtils.isBlank(request.getNickName())
                || StringUtils.isBlank(request.getEmail())) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        if (request.getNickName().length() > 11) {
            throw new CustomException(CommonCode.INVALID_PARAM);
        }
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new CustomException(CommonCode.USER_NOT_EXISTS);
        }
        user.setNickName(request.getNickName());
        user.setGender(request.getGender());
        user.setAge(this.getAgeByBirthday(request.getBirthday()));
        user.setBirthday(request.getBirthday());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRegion(request.getRegion());
        user.setIntroduction(request.getIntroduction());
        user.setUpdateTime(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        int result = userMapper.updateById(user);
        if (result > 0) {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(user, userInfo);
            return ResponseResult.success(userInfo);
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<String> updateAvatar(String userId, MultipartFile image) throws CustomException {
        if (StringUtils.isBlank(userId) || image == null || image.isEmpty()) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        if (!"image/png".equals(image.getContentType())
                && !"image/jpeg".equals(image.getContentType())) {
            throw new CustomException(CommonCode.UPLOAD_IMAGE_FORMAT_ERROR);
        }
        if (image.getSize() / 1024 / 1024 > 2) {
            throw new CustomException(CommonCode.UPLOAD_IMAGE_SIZE_ERROR);
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new CustomException(CommonCode.USER_NOT_EXISTS);
        }
        String filePath = "/avatar/" + userId;
        String filename = image.getOriginalFilename();
        if (StringUtils.isNotBlank(filename)) {
            filePath = filePath + filename.substring(filename.lastIndexOf("."));
        }
        ResponseResult<Object> result = fileFeignService.uploadFileToMinio(image, filePath);
        if (result.isSuccess()) {
            user.setAvatar(filePath);
            int update = userMapper.updateById(user);
            if (update > 0) {
                return ResponseResult.success(filePath);
            }
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Object> updateStatus(ChangeStatusRequest request) throws CustomException {
        if (request == null
                || request.getStatus() == null
                || StringUtils.isBlank(request.getUserId())) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new CustomException(CommonCode.USER_NOT_EXISTS);
        }
        user.setStatus(request.getStatus());
        int update = userMapper.updateById(user);
        if (update > 0) {
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Object> updatePassword(ChangePasswordRequest request) throws CustomException {
        if (request == null
                || StringUtils.isBlank(request.getUserId())
                || StringUtils.isBlank(request.getNewPassword())
                || StringUtils.isBlank(request.getCheckPassword())) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        if (request.getNewPassword().length() < 8
                || request.getNewPassword().length() > 16
                || !request.getNewPassword().equals(request.getCheckPassword())) {
            throw new CustomException(CommonCode.INVALID_PARAM);
        }
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new CustomException(CommonCode.USER_NOT_EXISTS);
        }
        user.setPassword(EncryptUtils.md5Base64(request.getNewPassword()));
        user.setUpdateTime(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        int result = userMapper.updateById(user);
        if (result > 0) {
            return ResponseResult.success();
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Object> updateTagsAdd(UpdateTagsRequest request) throws CustomException {
        if (request == null
                || StringUtils.isBlank(request.getUserId())
                || StringUtils.isBlank(request.getTag())) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        if (request.getTag().length() > 10) {
            throw new CustomException(CommonCode.INVALID_PARAM);
        }
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new CustomException(CommonCode.USER_NOT_EXISTS);
        }
        if (StringUtils.isBlank(user.getTags())) {
            user.setTags(request.getTag());
            int update = userMapper.updateById(user);
            if (update > 0) {
                return ResponseResult.success();
            }
        }
        if (StringUtils.isNotBlank(user.getTags())) {
            String[] tags = user.getTags().split(",");
            if (tags.length < 3) {
                user.setTags(user.getTags() + "," + request.getTag());
                int update = userMapper.updateById(user);
                if (update > 0) {
                    return ResponseResult.success();
                }
            }
        }
        return ResponseResult.fail();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Object> updateTagsRemove(UpdateTagsRequest request) throws CustomException {
        if (request == null
                || StringUtils.isBlank(request.getUserId())
                || StringUtils.isBlank(request.getTag())) {
            throw new CustomException(CommonCode.EMPTY_ERROR);
        }
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new CustomException(CommonCode.USER_NOT_EXISTS);
        }
        if (StringUtils.isBlank(user.getTags())) {
            return ResponseResult.success();
        }
        if (StringUtils.isNotBlank(user.getTags())) {
            String[] tags = user.getTags().split(",");
            List<String> tagList = new ArrayList<>(Arrays.asList(tags));
            tagList.remove(request.getTag());
            if (tagList.isEmpty()) {
                user.setTags("");
            }
            if (tagList.size() == 1) {
                user.setTags(tagList.get(0));
            }
            if (tagList.size() == 2) {
                user.setTags(tagList.get(0) + "," + tagList.get(1));
            }
            int update = userMapper.updateById(user);
            if (update > 0) {
                return ResponseResult.success();
            }
        }
        return ResponseResult.fail();
    }

    /**
     * 根据生日计算年龄
     *
     * @param birthdayStr 生日字符串：yyyy-MM-dd
     * @return 年龄
     */
    private int getAgeByBirthday(String birthdayStr) {
        if (StringUtils.isNotBlank(birthdayStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date birthday = sdf.parse(birthdayStr);
                //Calendar：日历
                /*从Calendar对象中或得一个Date对象*/
                Calendar cal = Calendar.getInstance();
                /*把出生日期放入Calendar类型的bir对象中，进行Calendar和Date类型进行转换*/
                Calendar bir = Calendar.getInstance();
                bir.setTime(birthday);
                /*如果生日大于当前日期，则抛出异常：出生日期不能大于当前日期*/
                if (cal.before(birthday)) {
                    throw new CustomException(CommonCode.INVALID_PARAM);
                }
                /*取出当前年月日*/
                int yearNow = cal.get(Calendar.YEAR);
                int monthNow = cal.get(Calendar.MONTH);
                int dayNow = cal.get(Calendar.DAY_OF_MONTH);
                /*取出出生年月日*/
                int yearBirth = bir.get(Calendar.YEAR);
                int monthBirth = bir.get(Calendar.MONTH);
                int dayBirth = bir.get(Calendar.DAY_OF_MONTH);
                /*大概年龄是当前年减去出生年*/
                int age = yearNow - yearBirth;
                /*如果出当前月小与出生月，或者当前月等于出生月但是当前日小于出生日，那么年龄age就减一岁*/
                if (monthNow < monthBirth || (monthNow == monthBirth && dayNow < dayBirth)) {
                    age--;
                }
                return age;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
}
