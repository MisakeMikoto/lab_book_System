package com.yiling.utils;

import com.yiling.controller.exception.BusinessException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/9 18:31
 */
public class DatePeriodUtils {

    public static String getDate(Date startTime, Date endTime){
        LocalDateTime startLocalTime = LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalTime = LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());
        int year = startLocalTime.getYear();
        Month month = startLocalTime.getMonth();
        String m = (month.getValue() >= 10) ? month.getValue()+"":"0"+month.getValue();
        int dayOfMonth = startLocalTime.getDayOfMonth();
        String d = (dayOfMonth >= 10) ? dayOfMonth+"":"0"+dayOfMonth;
        return year+"-"+m+"-"+d;
    }

    public static String getPeriod(Date startTime, Date endTime){
        LocalDateTime startLocalTime = LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault());
        LocalDateTime endLocalTime = LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault());
        int startminute = startLocalTime.getHour();
        int startsecond = startLocalTime.getMinute();
        int endminute = endLocalTime.getHour();
        int endsecond = endLocalTime.getMinute();
        String sm = (startminute >= 10) ? startminute+"":"0"+startminute;
        String ss = (startsecond >= 10) ? startsecond+"":"0"+startsecond;
        String em = (endminute >= 10) ? endminute+"":"0"+endminute;
        String es = (endsecond >= 10) ? endsecond+"":"0"+endsecond;
        return sm+":"+ss+"-"+em+":"+es;
    }

    public static Date getStartTime(String period,String date){
        String startTimeString = period.substring(0, 5) + ":00";
        startTimeString = date + " " + startTimeString;
        Date startTime = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            startTime = dateFormat.parse(startTimeString);
        } catch (Exception e) {
            throw new BusinessException(500, e, e.getMessage());
        }
        return startTime;
    }

    public static Date getEndTime(String period,String date){
        String endTimeString = period.substring(6, 11) + ":00";
        endTimeString = date + " " + endTimeString;
        Date endTime = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            endTime = dateFormat.parse(endTimeString);
        } catch (Exception e) {
            throw new BusinessException(500, e, e.getMessage());
        }
        return endTime;
    }

    public static String WeekTransform(Integer day){
        day = day % 7;
        if(day == 0) return "周一";
        if(day == 1) return "周二";
        if(day == 2) return "周三";
        if(day == 3) return "周四";
        if(day == 4) return "周五";
        if(day == 5) return "周六";
        if(day == 6) return "周日";
        return null;
    }
}
