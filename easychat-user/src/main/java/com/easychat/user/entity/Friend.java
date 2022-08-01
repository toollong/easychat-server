package com.easychat.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-04-19 13:00
 */
@TableName("easychat_friend")
@Data
public class Friend {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 所属用户 id
     */
    private String userId;
    /**
     * 会话 id
     */
    private String sessionId;
    /**
     * 会话创建时间
     */
    private String sessionTime;
    /**
     * 好友用户 id
     */
    private String friendUserId;
    /**
     * 好友备注
     */
    private String friendRemark;
    /**
     * 创建时间
     */
    private String createTime;

}
