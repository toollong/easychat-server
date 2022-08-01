package com.easychat.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-07-03 8:51
 */
@TableName("easychat_friend_verify")
@Data
public class FriendVerify {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    private String senderId;
    private String receiverId;
    private String applyReason;
    private String remark;
    /**
     * 验证状态：0未处理，1同意，2拒绝，3过期
     */
    private Integer status;
    /**
     * 是否已读：0未读，1已读
     */
    private Integer hasRead;
    private String createTime;

}
