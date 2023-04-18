package com.yiling.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/8 20:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResult {
    @TableField(value = "feedbackTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date time;

    private String title;

    private String detail;
}
