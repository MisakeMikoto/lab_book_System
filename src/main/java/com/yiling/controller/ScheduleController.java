package com.yiling.controller;

import com.yiling.controller.exception.BusinessException;
import com.yiling.controller.exception.Result;
import com.yiling.domain.*;
import com.yiling.service.*;
import com.yiling.utils.DatePeriodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:26
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @Autowired
    private LabService labService;

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/getOneById")
    public Result getSchedulesByLabId(Integer scheduleId) {
        Schedule oneByID = scheduleService.getOneByID(scheduleId);
        oneByID.setPeriod(DatePeriodUtils.getPeriod(oneByID.getStartTime(),oneByID.getEndTime()));
        return new Result(200,oneByID,"获取成功");
    }
    @GetMapping("/getAll")
    public Result getAll(){
        List<Schedule> all = scheduleService.getAll();
        ArrayList<ScheduleResult> scheduleResults = new ArrayList<>();
        for (Schedule schedule : all) {
            UserResult userResult = new UserResult();
            User user = userService.getUserById(schedule.getUserId());
            userResult.setUserId(user.getUserId());
            userResult.setAvatar(user.getAvatar());
            userResult.setUsername(user.getUsername());
            userResult.setRealName(user.getRealName());
            ScheduleResult scheduleResult = new ScheduleResult();
            scheduleResult.setLab(labService.getOneByLabId(schedule.getLabId()));
            scheduleResult.setPeriod(DatePeriodUtils.getPeriod(schedule.getStartTime(),schedule.getEndTime()));
            scheduleResult.setDate(DatePeriodUtils.getDate(schedule.getStartTime(),schedule.getEndTime()));
            scheduleResult.setUser(userResult);
            scheduleResult.setStartTime(schedule.getStartTime());
            scheduleResult.setEndTime(schedule.getEndTime());
            scheduleResults.add(scheduleResult);
        }
        return new Result(200,scheduleResults,"获取成功");
    }
    
    @GetMapping("/getByuserId")
    public Result getSC(HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        User user = userService.getUserById(Integer.valueOf(userId));
        UserResult userResult = new UserResult();
        userResult.setUserId(user.getUserId());
        userResult.setUsername(user.getUsername());
        userResult.setAvatar(user.getAvatar());
        userResult.setRealName(user.getRealName());
        List<Schedule> byUserId = scheduleService.getByUserId(Integer.valueOf(userId));
        ArrayList<ScheduleResult> scheduleResults = new ArrayList<>();
        for (Schedule schedule : byUserId) {
            ScheduleResult scheduleResult = new ScheduleResult();
            schedule.setDate(DatePeriodUtils.getDate(schedule.getStartTime(),schedule.getEndTime()));
            schedule.setPeriod(DatePeriodUtils.getPeriod(schedule.getStartTime(),schedule.getEndTime()));
            scheduleResult.setPeriod(schedule.getPeriod());
            scheduleResult.setDate(schedule.getDate());
            scheduleResult.setUser(userResult);
            scheduleResult.setEndTime(schedule.getEndTime());
            scheduleResult.setStartTime(schedule.getStartTime());
            scheduleResult.setLab(labService.getOneByLabId(schedule.getLabId()));
            scheduleResults.add(scheduleResult);
        }
        return new Result(200,scheduleResults,"获取成功");
    }

    @GetMapping("/getAllPending")
    public Result getAll(HttpServletRequest request){
        String userIdS = (String) request.getAttribute("userId");
        Integer userId = Integer.valueOf(userIdS);
        UserRole oneByUserId = userRoleService.getOneByUserId(userId);
        if(oneByUserId == null){
            throw new BusinessException(500,null,"需要管理员权限");
        }
        List<Reservation> allNoResolved = reservationService.getAllNoResolved();
        List<ScheduleResult> scheduleResults = new ArrayList<>();
        for (Reservation reservation : allNoResolved) {
            ScheduleResult scheduleResult = new ScheduleResult();
            UserResult userResult = new UserResult();
            User userById = userService.getUserById(reservation.getUserId());
            Lab oneByLabId = labService.getOneByLabId(reservation.getLabId());
            userResult.setUserId(userById.getUserId());
            userResult.setUsername(userById.getUsername());
            userResult.setAvatar(userById.getAvatar());
            userResult.setRealName(userById.getRealName());
            scheduleResult.setStartTime(reservation.getStartTime());
            scheduleResult.setEndTime(reservation.getEndTime());
            scheduleResult.setDate(DatePeriodUtils.getDate(reservation.getStartTime(),reservation.getEndTime()));
            scheduleResult.setPeriod(DatePeriodUtils.getPeriod(reservation.getStartTime(),reservation.getEndTime()));
            scheduleResult.setUser(userResult);
            scheduleResult.setLab(oneByLabId);
            scheduleResults.add(scheduleResult);

        }
        return new Result(200,scheduleResults,"获取成功");

    }

    @PostMapping("/addByPeriod")
    public Result addByPeriod(@RequestBody Schedule schedule, HttpServletRequest request) {
        try {
            schedule.setStartTime(DatePeriodUtils.getStartTime(schedule.getPeriod(),schedule.getDate()));
            schedule.setEndTime(DatePeriodUtils.getEndTime(schedule.getPeriod(),schedule.getDate()));
        } catch (Exception e) {
            throw new BusinessException(500, e, e.getMessage());
        }
        Lab oneByLabId = labService.getOneByLabId(schedule.getLabId());
        Feedback feedback = new Feedback();
        feedback.setUserId(schedule.getUserId());
        feedback.setFeedbackTime(new Date(System.currentTimeMillis()));
        feedback.setTitle("预约成功");
        feedback.setContent("您对于实验室" + oneByLabId.getLabName()+oneByLabId.getLocation()+ "于" + schedule.getStartTime() + "-" + schedule.getEndTime() + "的预约申请已经通过");

        List<Reservation> reservations = reservationService.checkExist(schedule);

        if (reservations == null || reservations.size() == 0) {
            throw new BusinessException(500, null, "预约不存在");
        }
        boolean b = scheduleService.add(schedule);
        for (Reservation reservation : reservations) {
            reservation.setStatus(1);
            reservationService.update(reservation);

        }
        if (b) {
            feedbackService.add(feedback);
            return new Result(200, b, "添加成功");
        }
        return new Result(500, b, "添加失败");
    }


    @PostMapping("/add")
    public Result add(@RequestBody Schedule schedule, HttpServletRequest request){

        List<Reservation> reservations = reservationService.checkExist(schedule);
        if(reservations == null || reservations.size() == 0){
            throw new BusinessException(500,null,"预约不存在");
        }
        Lab oneByLabId = labService.getOneByLabId(schedule.getLabId());
        boolean b = scheduleService.add(schedule);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Feedback feedback = new Feedback();
        feedback.setUserId(schedule.getUserId());
        feedback.setFeedbackTime(new Date(System.currentTimeMillis()));
        feedback.setTitle("预约成功");
        feedback.setContent("您对于实验室"+oneByLabId.getLabName()+oneByLabId.getLocation()+"于"+simpleDateFormat.format(schedule.getStartTime())+"-"+simpleDateFormat.format(schedule.getEndTime())+"的预约申请已经通过");
        feedbackService.add(feedback);
        for (Reservation reservation : reservations) {
            reservation.setStatus(1);
            reservationService.update(reservation);
        }

        if(b){
            return new Result(200,b,"添加成功");
        }
        return new Result(500,b,"添加失败");
    }
    @PostMapping("/delete")
    public Result delete(@RequestBody Schedule schedule){
        boolean b = scheduleService.delete(schedule);
        if(b){
            return new Result(200,b,"删除成功");
        }
        return new Result(500,b,"删除失败");
    }

    @PostMapping("/update")
    public Result update(@RequestBody Schedule schedule){
        boolean b = scheduleService.update(schedule);
        if(b){
            return new Result(200,b,"更新成功");
        }
        return new Result(500,b,"更新失败");
    }




}
