package com.yiling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiling.domain.Lab;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 16:50
 */
@Mapper
public interface LabDao extends BaseMapper<Lab> {
    @Select("SELECT * FROM lab\n")
    List<Lab> selectAll();
}
