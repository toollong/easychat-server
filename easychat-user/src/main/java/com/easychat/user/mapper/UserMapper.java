package com.easychat.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easychat.user.dto.UserInfo;
import com.easychat.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: long
 * @Date: 2022-04-18 12:17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名模糊查询用户列表
     *
     * @param username 用户名
     * @return 用户信息列表
     */
    @Select("SELECT id,username,nick_name,avatar,gender,region,introduction,tags " +
            "FROM easychat_user " +
            "WHERE username LIKE CONCAT('%',#{username},'%')")
    List<UserInfo> selectUserList(String username);
}
