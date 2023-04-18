package com.yiling.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import com.yiling.controller.exception.Result;
import com.yiling.dao.ReservationDao;
import com.yiling.domain.*;
import com.yiling.service.LabService;
import com.yiling.service.LabStatusService;
import com.yiling.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:25
 */
@RestController
@RequestMapping("/lab")
public class LabController {
    @Autowired
    private LabService labService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LabStatusService labStatusService;

    @GetMapping("/getAll")
    public Result getAllLabs() {
        List<Lab> allLabs = labService.getAllLabs();
        return new Result(200, allLabs, "获取成功");
    }

    @GetMapping("/getById")
    public Result getById(Integer labId, HttpServletRequest request){
        Lab oneByLabId = labService.getOneByLabId(labId);
        return new Result(200,oneByLabId,"获取成功");
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody Lab lab){
        boolean b = labService.deleteLab(lab.getLabId());
        if(b){
            return new Result(200,b,"删除成功");
        }
        return new Result(500,b,"删除失败");
    }

    @PostMapping("/update")
    public Result update(@RequestBody Lab lab){
        boolean b = labService.updateLab(lab);
        if(b){
            return new Result(200,b,"更新成功");
        }
        return new Result(500,b,"更新失败");
    }

    @PostMapping("/add")
    public Result add(@RequestBody Lab lab){
        boolean b = labService.addLab(lab);
        if(b){
            return new Result(200,b,"添加成功");
        }
        return new Result(500,b,"添加失败");
    }


    @GetMapping("/getSevenStatus")
    public Result getSevenStatus(Integer labId, Integer days, HttpServletRequest request){
        Lab lab = new Lab();
        lab.setLabId(labId);
        String attribute = (String) request.getAttribute("userId");
        List<LabPeriodStatusResult> status = labStatusService.getStatus(lab, Integer.valueOf(attribute),days);
        return new Result(200,status,"获取成功");

    }
    @GetMapping("/getStatusByWeek")
    public Result getStatusByWeek(Integer labId, Integer weekIndicator, HttpServletRequest request){
        Lab lab = new Lab();
        lab.setLabId(labId);
        String attribute = (String) request.getAttribute("userId");
        List<LabPeriodStatusResultWeek> statusByWeek = labStatusService.getStatusByWeek(lab, Integer.valueOf(attribute), 0, 6,weekIndicator);
        return new Result(200,statusByWeek,"获取成功");
    }

//    @GetMapping("/getStatusByWeek")
//    public Result getStatusByWeek(Integer labId, HttpServletRequest request){
//        Lab lab = new Lab();
//        lab.setLabId(labId);
//        String attribute = (String) request.getAttribute("userId");
//        List<LabPeriodStatusResultWeek> statusByWeek = labStatusService.getStatusByWeek(lab, Integer.valueOf(attribute), 0, 6);
//        return new Result(200,statusByWeek,"获取成功");
//    }


}
