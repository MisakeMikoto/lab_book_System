package com.yiling.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 16:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    @TableField(value = "feedbackId")
    private Integer feedbackId;
    @TableField(value = "userId")
    private Integer userId;
    @TableField(value = "content")
    private String content;
    @TableField(value = "feedbackTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date feedbackTime;
    private String title;
}
