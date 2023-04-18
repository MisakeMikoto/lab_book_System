package com.yiling.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/8 21:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResult {
    private Integer userId;
    private String avatar;
    private String username;
    private String realName;
}
