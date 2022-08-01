package com.easychat.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easychat.user.dto.FriendInfo;
import com.easychat.user.entity.Friend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: long
 * @Date: 2022-04-20 9:45
 */
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {

    /**
     * 根据用户 id 查询其好友信息列表
     *
     * @param userId 用户 id
     * @return 好友信息列表
     */
    List<FriendInfo> selectFriendInfoList(@Param("userId") String userId);

    /**
     * 根据用户 id 和好友用户 id 查询好友信息
     *
     * @param userId 用户 id
     * @param friendUserId 好友用户 id
     * @return 好友信息
     */
    FriendInfo selectFriendInfo(@Param("userId") String userId, @Param("friendUserId") String friendUserId);
}
