package com.yiling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiling.domain.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 16:51
 */
@Mapper
public interface ScheduleDao extends BaseMapper<Schedule> {
    @Select("SELECT * FROM schedule WHERE labId = #{labId}\n")
    List<Schedule> selectByLabId(Integer labId);

    @Select("SELECT * FROM schedule WHERE labId = #{labId} AND startTime BETWEEN #{startTime} AND #{endTime}")
    List<Schedule> getSchedulesByLabIdAndTimeRange(@Param("labId") Integer labId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Select("SELECT COUNT(*) FROM schedule WHERE startTime >= #{startTime} AND endTime <= #{endTime} AND labId = #{labId}\n")
    Integer getStatusSchedules(Date startTime, Date endTime, Integer labId);

    @Select("SELECT * FROM schedule WHERE startTime >= #{startTime} AND  labId = #{labId}\n")
    List<Schedule> seletctStatus(Date startTime, Integer labId);
}