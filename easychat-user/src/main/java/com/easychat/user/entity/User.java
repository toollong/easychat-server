package com.easychat.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-04-18 10:05
 */
@TableName("easychat_user")
@Data
public class User {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    private String username;
    private String password;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String avatar = "/avatar/default.jpg";
    /**
     * 性别（0 女，1 男，2 保密）
     */
    private Integer gender = 2;
    private Integer age;
    private String birthday;
    /**
     * 地区
     */
    private String region;
    private String email;
    private String phone;
    /**
     * 简介
     */
    private String introduction;
    /**
     * 状态（0 隐身，1 在线）
     */
    private Integer status = 1;
    /**
     * 标签
     */
    private String tags;
    /**
     * 加入的聊天组 id
     */
    private String groupIds;
    private String createTime;
    private String updateTime;

}
