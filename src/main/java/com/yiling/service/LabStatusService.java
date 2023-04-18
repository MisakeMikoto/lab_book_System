package com.yiling.service;

import com.yiling.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/5 20:40
 */
@Service
public interface LabStatusService {
    List<LabPeriodStatusResult> getStatus(Lab lab, Integer userId, Integer days);

    List<LabPeriodStatusResultWeek> getStatusByWeek(Lab lab, Integer userId, Integer start, Integer end,Integer weekIndicator);
}
