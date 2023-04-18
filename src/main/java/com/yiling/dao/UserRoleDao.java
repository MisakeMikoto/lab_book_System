package com.yiling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiling.domain.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 16:54
 */
@Mapper
public interface UserRoleDao extends BaseMapper<UserRole> {
    @Select("SELECT * FROM userRole WHERE userId = #{userId}\n")
    List<UserRole> selectByUserId(Integer userId);
}