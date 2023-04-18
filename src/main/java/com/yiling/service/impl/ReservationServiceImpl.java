package com.yiling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.dao.ReservationDao;
import com.yiling.domain.Reservation;
import com.yiling.domain.Schedule;
import com.yiling.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationDao reservationDao;

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = reservationDao.selectList(null);
        return reservations;
    }

    @Override
    public Reservation getOneById(Integer id) {
        LambdaQueryWrapper<Reservation> rlqw = new LambdaQueryWrapper<>();
        rlqw.eq(Reservation::getReservationId,id);
        Reservation reservation = reservationDao.selectOne(rlqw);
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) {
        LambdaUpdateWrapper<Reservation> rluw = new LambdaUpdateWrapper<>();
        rluw.eq(Reservation::getReservationId,reservation.getReservationId());
        int i = reservationDao.update(reservation,rluw);
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Reservation reservation) {
        LambdaQueryWrapper<Reservation> rlqw = new LambdaQueryWrapper<>();
        rlqw.eq(Reservation::getReservationId,reservation.getReservationId());
        int i = reservationDao.delete(rlqw);
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean add(Reservation reservation) {
        int insert = reservationDao.insert(reservation);
        if(insert == 1){
            return true;
        }
        return false;
    }

    @Override
    public List<Reservation> checkExist(Schedule schedule) {
        Date scheduleStartTime = schedule.getStartTime();
        Date scheduleEndTime = schedule.getEndTime();
        Integer labId = schedule.getLabId();
        Integer userId = schedule.getUserId();
        List<Reservation> reservations = reservationDao.checkExist(scheduleStartTime,scheduleEndTime,labId,userId);
        return reservations;
    }

    @Override
    public List<Reservation> getAllNoResolved() {
        List<Reservation> allNoResolveD = reservationDao.getAllNoResolveD(0);
        return allNoResolveD;
    }

    @Override
    public List<Reservation> getAllNoId(Reservation reservation) {
        List<Reservation> allNoId = reservationDao.getAllNoId(reservation.getStartTime(),reservation.getEndTime(),reservation.getLabId(),reservation.getUserId());
        return allNoId;
    }

    @Override
    public List<Reservation> getByUserId(Integer id) {
        LambdaQueryWrapper<Reservation> rlqw = new LambdaQueryWrapper<>();
        rlqw.eq(Reservation::getUserId,id);
        rlqw.eq(Reservation::getStatus,0);
        List<Reservation> reservations = reservationDao.selectList(rlqw);
        return reservations;
    }
}
