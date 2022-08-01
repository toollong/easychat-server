package com.easychat.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easychat.chat.dto.ChatInfo;
import com.easychat.chat.entity.ChatHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: long
 * @Date: 2022-04-20 9:46
 */
@Mapper
public interface ChatHistoryMapper extends BaseMapper<ChatHistory> {

    /**
     * 根据用户 id 查询聊天信息列表
     *
     * @param userId 用户 id
     * @return 聊天信息列表
     */
    List<ChatInfo> selectChatInfoList(@Param("userId") String userId);

    /**
     * 根据用户 id 和好友用户 id 查询聊天信息
     *
     * @param userId 用户 id
     * @param friendUserId 好友用户 id
     * @return 聊天信息
     */
    ChatInfo selectChatInfo(@Param("userId") String userId, @Param("friendUserId") String friendUserId);

    /**
     * 将用户在会话中接收到的所有未读消息设为已读
     *
     * @param sessionId 会话 id
     * @param userId 用户 id
     * @return 更新记录数
     */
    @Update("UPDATE easychat_history " +
            "SET has_read = 1 " +
            "WHERE session_id = #{sessionId} AND receiver_id = #{userId}")
    int updateChatHistory(String sessionId, String userId);
}
