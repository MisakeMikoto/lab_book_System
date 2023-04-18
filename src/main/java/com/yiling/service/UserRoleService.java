package com.yiling.service;

import com.yiling.domain.UserRole;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:13
 */
public interface UserRoleService {
    List<UserRole> getAllRole();

    UserRole getOneByUserId(Integer id);

    boolean updateRole(UserRole userRole);

    boolean removeRole(UserRole userRole);

    boolean addRole(UserRole userRole);
}
