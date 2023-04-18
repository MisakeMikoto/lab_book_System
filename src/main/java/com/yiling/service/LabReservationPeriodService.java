package com.yiling.service;

import com.yiling.domain.LabReservationPeriod;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/6 14:37
 */
public interface LabReservationPeriodService {

    boolean add(LabReservationPeriod labReservationPeriod);

    List<LabReservationPeriod> getAll();

    boolean delete(LabReservationPeriod labReservationPeriod);

    boolean update(LabReservationPeriod labReservationPeriod);


}
