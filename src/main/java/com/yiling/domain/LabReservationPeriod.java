package com.yiling.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.util.Date;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/6 14:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("labReservationPeriod")
public class LabReservationPeriod {

    @TableField(value = "periodId")
    private Integer periodId;

    @TableField(value = "startTime")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss",timezone="GMT+8")
    private Time startTime;

    @TableField(value = "endTime")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss",timezone="GMT+8")
    private Time endTime;
}
