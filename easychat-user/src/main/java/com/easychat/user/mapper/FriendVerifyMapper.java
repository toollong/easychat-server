package com.easychat.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easychat.user.dto.VerifyInfo;
import com.easychat.user.entity.FriendVerify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: long
 * @Date: 2022-07-03 9:37
 */
@Mapper
public interface FriendVerifyMapper extends BaseMapper<FriendVerify> {

    /**
     * 根据用户 id 查询其好友验证信息列表
     *
     * @param userId 用户 id
     * @return 好友验证信息列表
     */
    List<VerifyInfo> selectVerifyInfoList(@Param("userId") String userId);

    /**
     * 根据发送者 id 和接收者 id 查询好友验证信息
     *
     * @param senderId 发送者 id
     * @param receiverId 接收者 id
     * @return 好友验证信息
     */
    VerifyInfo selectVerifyInfo(@Param("senderId") String senderId,
                                @Param("receiverId") String receiverId);

    /**
     * 将用户接收到的所有未读好友验证更新为已读
     *
     * @param userId 用户 id
     * @return 更新记录数
     */
    @Update("UPDATE easychat_friend_verify " +
            "SET has_read = 1 " +
            "WHERE receiver_id = #{userId}")
    int updateFriendVerify(String userId);
}
