package com.yiling.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.yiling.domain.Reservation;
import com.yiling.domain.Schedule;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:13
 */
public interface ReservationService {
    List<Reservation> getAll();

    Reservation getOneById(Integer id);

    boolean update(Reservation reservation);

    boolean delete(Reservation reservation);

    boolean add(Reservation reservation);

    List<Reservation> checkExist(Schedule schedule);

    List<Reservation> getAllNoResolved();

    List<Reservation> getAllNoId(Reservation reservation);

    List<Reservation> getByUserId(Integer id);


}
