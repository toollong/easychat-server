package com.easychat.chat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-04-19 13:40
 */
@TableName("easychat_history")
@Data
public class ChatHistory {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 发送者 id
     */
    private String senderId;
    /**
     * 接收者 id
     */
    private String receiverId;
    /**
     * 所属会话 id
     */
    private String sessionId;
    /**
     * 内容类型（0 文本，1 图片，2 文件，3 语音）
     */
    private Integer type;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 是否显示时间（1 显示，0 不显示）
     */
    private Integer showTime;
    /**
     * 是否已读（1 已读，0 未读）
     */
    private Integer hasRead;

}
