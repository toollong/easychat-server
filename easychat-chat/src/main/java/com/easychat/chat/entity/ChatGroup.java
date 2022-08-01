package com.easychat.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Set;

/**
 * @Author: long
 * @Date: 2022-04-18 10:21
 */
@TableName("easychat_group")
@Data
public class ChatGroup {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 聊天组名
     */
    private String groupName;
    /**
     * 成员 id
     */
    private Set<String> memberIds;
    /**
     * 创建时间
     */
    private String createTime;

}
