package com.yiling.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.yiling.domain.Reservation;
import com.yiling.domain.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 16:50
 */
@Mapper
public interface ReservationDao extends BaseMapper<Reservation> {
    @Select("SELECT * FROM reservation WHERE userId = #{userId}\n")
    List<Reservation> selectByUserId(Integer userId);
    @Select("SELECT * FROM reservation WHERE labId = #{labId}\n")
    List<Reservation> selectByLabId(Integer labId);

    @Select("SELECT COUNT(*) FROM reservation WHERE startTime >= #{startTime} AND endTime <= #{endTime} AND labId = #{labId} AND userId = #{userId}\n")
    Integer checkStatus(Date startTime, Date endTime, Integer labId, Integer userId);

    @Select("SELECT * FROM reservation WHERE startTime >= #{startTime} AND labId = #{labId} AND userId = #{userId}\n")
    List<Reservation> selectStatus(Date startTime, Integer labId, Integer userId);

    @Select("SELECT  * FROM reservation WHERE  startTime = #{startTime} AND endTime = #{endTime} AND labId = #{labId} AND userId = #{userId}")
    List<Reservation> checkExist(Date startTime, Date endTime, Integer labId, Integer userId);

    @Select("SELECT * FROM reservation WHERE status = #{Status}")
    List<Reservation> getAllNoResolveD(Integer Status);

    @Select("SELECT * FROM reservation WHERE startTime = #{startTime} AND endTime = #{endTime} AND labId = #{labId} AND userId = #{userId}")
    List<Reservation> getAllNoId(Date startTime, Date endTime, Integer labId, Integer userId);

}