package com.yiling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dao.*;
import com.yiling.domain.*;
import com.yiling.service.LabStatusService;
import com.yiling.utils.DatePeriodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/5 20:41
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LabStatusServiceImpl implements LabStatusService {
    @Autowired
    private ScheduleDao scheduleDao;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private LabDao labDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LabReservationPeriodDao labReservationPeriodDao;

    @Override
    public List<LabPeriodStatusResult> getStatus(Lab lab , Integer userId, Integer days) {
        ArrayList<LocalDateTime> startTimes = new ArrayList<>();
        ArrayList<LocalDateTime> endTimes = new ArrayList<>();
        ArrayList<LabStatus> labStatuses = new ArrayList<>();
        ArrayList<LabPeriodStatusResult> labPeriodStatusResults = new ArrayList<>();

        LambdaQueryWrapper<User> ulqw = new LambdaQueryWrapper<>();
        LocalDate now = LocalDate.now();
        LocalDateTime theStartOfToday = now.atStartOfDay();
        now = theStartOfToday.toLocalDate();
        List<LabReservationPeriod> periods = labReservationPeriodDao.selectList(null);

        for (LabReservationPeriod period : periods) {
            LocalDateTime startDateTime = LocalDateTime.of(now, period.getStartTime().toLocalTime());
            LocalDateTime endDateTime = LocalDateTime.of(now, period.getEndTime().toLocalTime());
            startTimes.add(startDateTime);
            endTimes.add(endDateTime);
        }
        List<Reservation> reservations = reservationDao.selectStatus(Date.from(theStartOfToday.atZone(ZoneId.systemDefault()).toInstant()), lab.getLabId(), userId);
        List<Schedule> schedules = scheduleDao.seletctStatus(Date.from(theStartOfToday.atZone(ZoneId.systemDefault()).toInstant()), lab.getLabId());


        int size  = periods.size();
        for (int i = 0; i < days; i++) {
            for (int j = 0; j < size; j++) {
                LabStatus labStatus = new LabStatus();
                LabPeriodStatusResult labPeriodStatusResult = new LabPeriodStatusResult();
                labStatus.setLabId(lab.getLabId());
                labStatus.setUserId(userId);

                LocalDateTime startTime = startTimes.get(j).plusDays(i);
                LocalDateTime endTime = endTimes.get(j).plusDays(i);


                int year = startTime.getYear();
                Month month = startTime.getMonth();
                String m = (month.getValue() >= 10) ? month.getValue()+"":"0"+month.getValue();
                int dayOfMonth = startTime.getDayOfMonth();
                String d = (dayOfMonth >= 10) ? dayOfMonth+"":"0"+dayOfMonth;
                labStatus.setDate(year+"-"+m+"-"+d);
                int startminute = startTime.getHour();
                int startsecond = startTime.getMinute();
                int endminute = endTime.getHour();
                int endsecond = endTime.getMinute();
                String sm = (startminute >= 10) ? startminute+"":"0"+startminute;
                String ss = (startsecond >= 10) ? startsecond+"":"0"+startsecond;
                String em = (endminute >= 10) ? endminute+"":"0"+endminute;
                String es = (endsecond >= 10) ? endsecond+"":"0"+endsecond;
                labStatus.setPeriod(sm+":"+ss+"-"+em+":"+es);

                // 状态设置
                for (Reservation reservation : reservations) {
                    Date reservationStartTime = reservation.getStartTime();
                    Date reservationEndTime = reservation.getEndTime();
                    LocalDateTime reservationStartLocalTime = LocalDateTime.ofInstant(reservationStartTime.toInstant(), ZoneId.systemDefault());
                    LocalDateTime reservationEndLocalTime = LocalDateTime.ofInstant(reservationEndTime.toInstant(), ZoneId.systemDefault());
                    int flagStart = reservationStartLocalTime.compareTo(startTime);
                    int flagEnd = reservationEndLocalTime.compareTo(endTime);
//                    System.out.println("Reservation:"+"flagStart="+flagStart+"  flagEnd="+flagEnd);
                    if(flagStart <= 0 && flagEnd >= 0){
                        labStatus.setStatus(1);
                        break;
                    }
                }

                for (Schedule schedule : schedules) {
                    Date scheduleStartTime = schedule.getStartTime();
                    Date scheduleEndTime = schedule.getEndTime();
                    LocalDateTime scheduleStartLocalTime = LocalDateTime.ofInstant(scheduleStartTime.toInstant(), ZoneId.systemDefault());
                    LocalDateTime scheduleEndLocalTime = LocalDateTime.ofInstant(scheduleEndTime.toInstant(), ZoneId.systemDefault());
                    int flagStart = scheduleStartLocalTime.compareTo(startTime);
                    int flagEnd = scheduleEndLocalTime.compareTo(endTime);
//                    System.out.println("Schedule:"+"flagStart="+flagStart+"  flagEnd="+flagEnd);
                    if(flagStart <= 0 && flagEnd >= 0){
                        labStatus.setStatus(2);
                        break;
                    }
                }


                labStatuses.add(labStatus);

                labPeriodStatusResult.setStatus(labStatus.getStatus());
                labPeriodStatusResult.setPeriod(labStatus.getPeriod());
                labPeriodStatusResult.setDate(labStatus.getDate());

                labPeriodStatusResults.add(labPeriodStatusResult);
            }
        }
        return labPeriodStatusResults;
    }

    @Override
    public List<LabPeriodStatusResultWeek> getStatusByWeek(Lab lab, Integer userId, Integer start, Integer end, Integer weekIndicator) {
        int mindays = weekIndicator * 7;
        ArrayList<LocalDateTime> startTimes = new ArrayList<>();
        ArrayList<LocalDateTime> endTimes = new ArrayList<>();
        ArrayList<LabStatus> labStatuses = new ArrayList<>();
        ArrayList<LabPeriodStatusResultWeek> labPeriodStatusResultWeeks = new ArrayList<>();

        LambdaQueryWrapper<User> ulqw = new LambdaQueryWrapper<>();
        LocalDate now = LocalDate.now();
        int value = now.getDayOfWeek().getValue();
        LocalDateTime theStartOfToday = now.atStartOfDay();
        theStartOfToday = theStartOfToday.minusDays(value - 1);
        if(mindays >= 0){
            theStartOfToday = theStartOfToday.plusDays(mindays);
        }else{
            theStartOfToday = theStartOfToday.minusDays(-mindays);
        }
        now = theStartOfToday.toLocalDate();
        List<LabReservationPeriod> periods = labReservationPeriodDao.selectList(null);

        for (LabReservationPeriod period : periods) {
            LocalDateTime startDateTime = LocalDateTime.of(now, period.getStartTime().toLocalTime());
            LocalDateTime endDateTime = LocalDateTime.of(now, period.getEndTime().toLocalTime());
            startTimes.add(startDateTime);
            endTimes.add(endDateTime);
        }
        List<Reservation> reservations = reservationDao.selectStatus(Date.from(theStartOfToday.atZone(ZoneId.systemDefault()).toInstant()), lab.getLabId(), userId);
        List<Schedule> schedules = scheduleDao.seletctStatus(Date.from(theStartOfToday.atZone(ZoneId.systemDefault()).toInstant()), lab.getLabId());


        int size  = periods.size();
        for (int i = start; i <= end; i++) {
            for (int j = 0; j < size; j++) {
                LabStatus labStatus = new LabStatus();
                LabPeriodStatusResultWeek labPeriodStatusResultWeek = new LabPeriodStatusResultWeek();
                labStatus.setLabId(lab.getLabId());
                labStatus.setUserId(userId);

                LocalDateTime startTime = startTimes.get(j).plusDays(i);
                LocalDateTime endTime = endTimes.get(j).plusDays(i);


                int year = startTime.getYear();
                Month month = startTime.getMonth();
                String m = (month.getValue() >= 10) ? month.getValue()+"":"0"+month.getValue();
                int dayOfMonth = startTime.getDayOfMonth();
                String d = (dayOfMonth >= 10) ? dayOfMonth+"":"0"+dayOfMonth;
                labStatus.setDate(year+"-"+m+"-"+d);
                int startminute = startTime.getHour();
                int startsecond = startTime.getMinute();
                int endminute = endTime.getHour();
                int endsecond = endTime.getMinute();
                String sm = (startminute >= 10) ? startminute+"":"0"+startminute;
                String ss = (startsecond >= 10) ? startsecond+"":"0"+startsecond;
                String em = (endminute >= 10) ? endminute+"":"0"+endminute;
                String es = (endsecond >= 10) ? endsecond+"":"0"+endsecond;
                labStatus.setPeriod(sm+":"+ss+"-"+em+":"+es);

                // 状态设置
                for (Reservation reservation : reservations) {
                    Date reservationStartTime = reservation.getStartTime();
                    Date reservationEndTime = reservation.getEndTime();
                    LocalDateTime reservationStartLocalTime = LocalDateTime.ofInstant(reservationStartTime.toInstant(), ZoneId.systemDefault());
                    LocalDateTime reservationEndLocalTime = LocalDateTime.ofInstant(reservationEndTime.toInstant(), ZoneId.systemDefault());
                    int flagStart = reservationStartLocalTime.compareTo(startTime);
                    int flagEnd = reservationEndLocalTime.compareTo(endTime);
//                    System.out.println("Reservation:"+"flagStart="+flagStart+"  flagEnd="+flagEnd);
                    if(flagStart <= 0 && flagEnd >= 0){
                        labStatus.setStatus(1);
                        break;
                    }
                }

                for (Schedule schedule : schedules) {
                    Date scheduleStartTime = schedule.getStartTime();
                    Date scheduleEndTime = schedule.getEndTime();
                    LocalDateTime scheduleStartLocalTime = LocalDateTime.ofInstant(scheduleStartTime.toInstant(), ZoneId.systemDefault());
                    LocalDateTime scheduleEndLocalTime = LocalDateTime.ofInstant(scheduleEndTime.toInstant(), ZoneId.systemDefault());
                    int flagStart = scheduleStartLocalTime.compareTo(startTime);
                    int flagEnd = scheduleEndLocalTime.compareTo(endTime);
//                    System.out.println("Schedule:"+"flagStart="+flagStart+"  flagEnd="+flagEnd);
                    if(flagStart <= 0 && flagEnd >= 0){
                        labStatus.setStatus(2);
                        break;
                    }
                }


                labStatuses.add(labStatus);

                labPeriodStatusResultWeek.setStatus(labStatus.getStatus());
                labPeriodStatusResultWeek.setPeriod(labStatus.getPeriod());
                labPeriodStatusResultWeek.setDate(labStatus.getDate());
                labPeriodStatusResultWeek.setStartTime(DatePeriodUtils.getStartTime(labPeriodStatusResultWeek.getPeriod(),labPeriodStatusResultWeek.getDate()));
                labPeriodStatusResultWeek.setEndTime(DatePeriodUtils.getEndTime(labPeriodStatusResultWeek.getPeriod(),labPeriodStatusResultWeek.getDate()));
                labPeriodStatusResultWeek.setWeek(i + 1);

                labPeriodStatusResultWeeks.add(labPeriodStatusResultWeek);
            }
        }
        return labPeriodStatusResultWeeks;
    }

}
