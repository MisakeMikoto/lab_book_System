package com.yiling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.controller.exception.BusinessException;
import com.yiling.dao.LabDao;
import com.yiling.dao.ReservationDao;
import com.yiling.dao.ScheduleDao;
import com.yiling.dao.UserDao;
import com.yiling.domain.Lab;
import com.yiling.domain.Reservation;
import com.yiling.domain.Schedule;
import com.yiling.domain.User;
import com.yiling.service.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LabServiceImpl implements LabService {
    @Autowired
    private LabDao labDao;

    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ReservationDao reservationDao;

    @Override
    public List<Lab> getAllLabs() {
        List<Lab> labs = labDao.selectAll();
        return labs;
    }

    @Override
    public boolean addLab(Lab lab) {
        if(lab == null){
            throw new BusinessException(500,null,"空参传递");
        }
        int insert = labDao.insert(lab);
        if(insert == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteLab(Integer id) {
        LambdaQueryWrapper<Lab> llqw = new LambdaQueryWrapper<>();
        llqw.eq(Lab::getLabId,id);
        int i = labDao.delete(llqw);
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateLab(Lab lab) {
        LambdaUpdateWrapper<Lab> luw = new LambdaUpdateWrapper<>();
        luw.eq(Lab::getLabId,lab.getLabId());
        int i = labDao.update(lab,luw);
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public Lab getOneByLabId(Integer id) {
        LambdaQueryWrapper<Lab> llqw = new LambdaQueryWrapper<>();
        llqw.eq(Lab::getLabId,id);
        Lab lab = labDao.selectOne(llqw);
        return lab;
    }


}
