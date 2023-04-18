package com.yiling.service;

import com.yiling.domain.Lab;
import com.yiling.domain.User;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:13
 */
public interface LabService {
    List<Lab> getAllLabs();

    boolean addLab(Lab lab);

    boolean deleteLab(Integer id);

    boolean updateLab(Lab lab);

    Lab getOneByLabId(Integer id);

}
