package com.yiling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.dao.LabReservationPeriodDao;
import com.yiling.domain.LabReservationPeriod;
import com.yiling.service.LabReservationPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/6 14:42
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LabReservationPeriodServiceImpl implements LabReservationPeriodService {
    @Autowired
    private LabReservationPeriodDao labReservationPeriodDao;

    @Override
    public boolean add(LabReservationPeriod labReservationPeriod) {
        int insert = labReservationPeriodDao.insert(labReservationPeriod);
        if(insert == 1){
            return true;
        }
        return false;
    }

    @Override
    public List<LabReservationPeriod> getAll() {
        List<LabReservationPeriod> periods = labReservationPeriodDao.selectList(null);
        return periods;
    }

    @Override
    public boolean delete(LabReservationPeriod labReservationPeriod) {
        LambdaQueryWrapper<LabReservationPeriod> lrplqw = new LambdaQueryWrapper<>();
        lrplqw.eq(LabReservationPeriod::getPeriodId,labReservationPeriod.getPeriodId());
        int delete = labReservationPeriodDao.delete(lrplqw);
        if(delete == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(LabReservationPeriod labReservationPeriod) {
        LambdaUpdateWrapper<LabReservationPeriod> lrpluw = new LambdaUpdateWrapper<>();
        lrpluw.eq(LabReservationPeriod::getPeriodId,labReservationPeriod.getPeriodId());
        int update = labReservationPeriodDao.update(labReservationPeriod, lrpluw);
        if(update == 1){
            return true;
        }
        return false;
    }
}
