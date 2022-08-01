package com.easychat.chat.dto;

import com.easychat.chat.entity.ChatHistory;
import lombok.Data;

/**
 * @Author: long
 * @Date: 2022-04-20 17:33
 */
@Data
public class ChatInfo {

    private String userId;
    private String sessionId;
    private String sessionTime;
    private String friendUserId;
    private String friendRemark;
    private String friendNickName;
    private String friendAvatar;
    private ChatHistory latestChatHistory = new ChatHistory();

}
