package com.yiling.controller;

import com.yiling.controller.exception.BusinessException;
import com.yiling.controller.exception.Result;
import com.yiling.domain.*;
import com.yiling.service.*;
import com.yiling.utils.DatePeriodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:26
 */
@RestController
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private LabService labService;

    @Autowired
    private UserService userService;

    @GetMapping("/getByuserId")
    public Result getReservation(HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        User user = userService.getUserById(Integer.valueOf(userId));
        UserResult userResult = new UserResult();
        userResult.setAvatar(user.getAvatar());
        userResult.setUsername(user.getUsername());
        userResult.setUserId(user.getUserId());
        List<Reservation> byUserId = reservationService.getByUserId(Integer.valueOf(userId));
        List<ScheduleResult> scheduleResults = new ArrayList<>();
        for (Reservation reservation : byUserId) {
            ScheduleResult scheduleResult = new ScheduleResult();
            reservation.setDate(DatePeriodUtils.getDate(reservation.getStartTime(),reservation.getEndTime()));
            reservation.setPeriod(DatePeriodUtils.getPeriod(reservation.getStartTime(),reservation.getEndTime()));
            scheduleResult.setPeriod(reservation.getPeriod());
            scheduleResult.setDate(reservation.getDate());
            scheduleResult.setLab(labService.getOneByLabId(reservation.getLabId()));
            scheduleResult.setUser(userResult);
            scheduleResult.setStartTime(reservation.getStartTime());
            scheduleResult.setEndTime(reservation.getEndTime());
            scheduleResults.add(scheduleResult);
        }
        return new Result(200,scheduleResults,"获取成功");
    }

    @GetMapping("/getOneById")
    public Result getSchedulesByLabId(Integer reservationId) {
        Reservation oneByID = reservationService.getOneById(reservationId);
        return new Result(200,oneByID,"获取成功");
    }

    @GetMapping("/getAll")
    public Result getAll(){
        List<Reservation> all = reservationService.getAll();
        for (Reservation reservation : all) {
            reservation.setPeriod(DatePeriodUtils.getPeriod(reservation.getStartTime(),reservation.getEndTime()));
            reservation.setDate(DatePeriodUtils.getDate(reservation.getStartTime(),reservation.getEndTime()));
        }
        return new Result(200,all,"获取成功");
    }

    @PostMapping("/addByPeriod")
    public Result addByPeriod(@RequestBody Reservation reservation, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        String date = reservation.getDate();
        String period = reservation.getPeriod();
        String startTimeString = period.substring(0, 5)+":00";
        String endTimeString = period.substring(6, 11)+":00";
        startTimeString = date + " " + startTimeString;
        endTimeString = date + " " + endTimeString;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startTime = dateFormat.parse(startTimeString);
            Date endTime = dateFormat.parse(endTimeString);
            reservation.setStartTime(startTime);
            reservation.setEndTime(endTime);
            reservation.setUserId(Integer.valueOf(userId));
        } catch (Exception e) {
            throw new BusinessException(500,e,e.getMessage());
        }
        boolean b = reservationService.add(reservation);
        if(b){
            return new Result(200,b,"添加成功");
        }
        return new Result(500,b,"添加失败");
    }

    @PostMapping("/add")
    public Result add(@RequestBody Reservation reservation, HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        reservation.setUserId(Integer.valueOf(userId));
        boolean b = reservationService.add(reservation);
        if(b){
            return new Result(200,b,"添加成功");
        }
        return new Result(500,b,"添加失败");
    }

        @PostMapping("/delete")
        public Result delete(@RequestBody Reservation reservation, HttpServletRequest request){
            String S = (String) request.getAttribute("userId");
            Integer userId = Integer.valueOf(S);

            UserRole oneByUserId = userRoleService.getOneByUserId(userId);
            if(oneByUserId == null){
                throw new BusinessException(500,"该操作需要管理员权限");
            }

            List<Reservation> allNoId = reservationService.getAllNoId(reservation);
            reservation = allNoId.get(0);
            Lab oneByLabId = labService.getOneByLabId(reservation.getLabId());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Feedback feedback = new Feedback();
            feedback.setFeedbackTime(new Date(System.currentTimeMillis()));
            feedback.setTitle("预约失败");
            feedback.setContent("您对于实验室"+oneByLabId.getLabName()+oneByLabId.getLocation()+"于"+simpleDateFormat.format(reservation.getStartTime())+"-"+simpleDateFormat.format(reservation.getEndTime())+"的预约已被取消");
            feedback.setUserId(reservation.getUserId());

            boolean b = false;
            if(reservation.getUserId() == userId || oneByUserId != null){
                b = reservationService.delete(reservation);
            }
            if(b){
                if(oneByUserId != null){
                    feedbackService.add(feedback);
                }
                return new Result(200,b,"删除成功");
            }
            return new Result(500,b,"删除失败");
        }

        @PostMapping("/update")
        public Result update(@RequestBody Reservation reservation){
            boolean b = reservationService.update(reservation);
            if(b){
                return new Result(200,b,"更新成功");
            }
            return new Result(500,b,"更新失败");
        }
}
