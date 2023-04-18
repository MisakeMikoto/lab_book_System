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

import java.util.Date;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 16:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("reservation")
public class Reservation {
    @TableField(value = "reservationId")
    private Integer reservationId;
    @TableField(value = "userId")
    private Integer userId;
    @TableField(value = "labId")
    private Integer labId;
    @TableField(value = "startTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    @TableField(value = "endTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;
    @TableField(value = "status")
    private Integer status = 0;
    @TableField(exist = false)
    private String date;
    @TableField(exist = false)
    private String period;
}
