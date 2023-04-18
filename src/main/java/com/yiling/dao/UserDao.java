package com.yiling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiling.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 16:46
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
    @Select("SELECT * FROM user\n")
    List<User> selectAll();
    @Select("SELECT * FROM user WHERE phoneNumber = #{phoneNumber}\n")
    User selectByPhoneNumber(String phoneNumber);
    @Select("SELECT * FROM user WHERE email = #{email}\n")
    User selectByEmail(String email);
}
