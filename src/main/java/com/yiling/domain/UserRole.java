package com.yiling.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 16:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "userRole")
public class UserRole {
    @TableField(value = "userRoleId")
    private Integer userRoleId;
    @TableField(value = "userId")
    private Integer userId;
    @TableField(value = "roleName")
    private String roleName;

}
