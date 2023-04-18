package com.yiling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiling.domain.Feedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 16:56
 */
@Mapper
public interface FeedbackDao extends BaseMapper<Feedback> {
    @Select("SELECT * FROM feedback WHERE userId = #{userId}\n")
    List<Feedback> selectByUserId(Integer userId);
}