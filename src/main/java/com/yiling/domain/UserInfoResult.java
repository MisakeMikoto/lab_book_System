package com.yiling.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/8 20:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResult {
    private Integer userId;
    private String email;
    private String username;
    private String avatar;
    private String realName;
}
