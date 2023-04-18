package com.yiling.service;

import com.yiling.domain.Schedule;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:13
 */
public interface ScheduleService {
    List<Schedule> getAll();

    Schedule getOneByID(Integer id);

    boolean delete(Schedule schedule);

    boolean update(Schedule schedule);

    boolean add(Schedule schedule);

    List<Schedule> getByUserId(Integer id);
}
