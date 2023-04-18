package com.yiling.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 16:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lab {
    @TableField(value = "labId")
    private Integer labId;
    @TableField(value = "labName")
    private String labName;
    @TableField(value = "location")
    private String location;
    @TableField(value = "contact")
    private String contact;
}
