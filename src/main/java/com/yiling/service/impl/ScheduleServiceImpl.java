package com.yiling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.dao.ScheduleDao;
import com.yiling.domain.Schedule;
import com.yiling.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleDao scheduleDao;


    @Override
    public List<Schedule> getAll() {
        List<Schedule> schedules = scheduleDao.selectList(null);
        return schedules;
    }

    @Override
    public Schedule getOneByID( Integer id) {
        LambdaQueryWrapper<Schedule> slwq = new LambdaQueryWrapper<>();
        slwq.eq(Schedule::getScheduleId,id);
        Schedule schedule = scheduleDao.selectOne(slwq);
        return schedule;
    }

    @Override
    public boolean delete(Schedule schedule) {
        LambdaQueryWrapper<Schedule> slwq = new LambdaQueryWrapper<>();
        slwq.eq(Schedule::getScheduleId,schedule.getScheduleId());
        int i = scheduleDao.delete(slwq);
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Schedule schedule) {
        LambdaUpdateWrapper<Schedule> sluw = new LambdaUpdateWrapper<>();
        sluw.eq(Schedule::getScheduleId,schedule.getScheduleId());
        int i = scheduleDao.update(schedule,sluw);
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean add(Schedule schedule) {
        int insert = scheduleDao.insert(schedule);
        if(insert == 1){
            return true;
        }
        return false;
    }

    @Override
    public List<Schedule> getByUserId(Integer id) {
        LambdaQueryWrapper<Schedule> slqw = new LambdaQueryWrapper<>();
        slqw.eq(Schedule::getUserId,id);
        return scheduleDao.selectList(slqw);
    }
}
