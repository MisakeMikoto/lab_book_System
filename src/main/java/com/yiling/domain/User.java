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
 * @Date 2023/4/4 16:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    @TableField(value = "userId")
    private Integer userId;
    @TableField(value = "username")
    private String username;
    @TableField(value = "phoneNumber")
    private String phoneNumber;
    @TableField(value = "email")
    private String email;
    @TableField(value = "password")
    private String password;
    @TableField(exist = false)
    private String code;
    @TableField(value = "avatar")
    private String avatar;
    @TableField(value = "realName")
    private String realName;

}
