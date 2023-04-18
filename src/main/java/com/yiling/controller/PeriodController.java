package com.yiling.controller;

import com.yiling.controller.exception.Result;
import com.yiling.domain.LabReservationPeriod;
import com.yiling.service.LabReservationPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/6 14:48
 */
@RestController
@RequestMapping("/period")
public class PeriodController {
    @Autowired
    private LabReservationPeriodService labReservationPeriodService;

    @GetMapping("/getAll")
    public Result getAll(){
        List<LabReservationPeriod> all = labReservationPeriodService.getAll();
        return new Result(200,all,"获取成功");
    }

    @PostMapping("/add")
    public Result add(@RequestBody LabReservationPeriod labReservationPeriod){
        boolean add = labReservationPeriodService.add(labReservationPeriod);
        if(add){
            return new Result(200,add,"添加成功");
        }
        return new Result(500,add,"添加失败");
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody LabReservationPeriod labReservationPeriod){
        boolean delete = labReservationPeriodService.delete(labReservationPeriod);
        if(delete){
            return new Result(200,delete,"删除成功");
        }
        return new Result(500,delete,"删除失败");
    }

    @PostMapping("/update")
    public Result update(@RequestBody LabReservationPeriod labReservationPeriod){
        boolean update = labReservationPeriodService.update(labReservationPeriod);
        if(update){
            return new Result(200,update,"更新成功");
        }
        return new Result(500,update,"更新失败");
    }

}
