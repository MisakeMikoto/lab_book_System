package com.yiling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.controller.exception.BusinessException;
import com.yiling.dao.UserRoleDao;
import com.yiling.domain.UserRole;
import com.yiling.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleDao userRoleDao;


    @Override
    public List<UserRole> getAllRole() {
        List<UserRole> userRoles = userRoleDao.selectList(null);
        return userRoles;
    }

    @Override
    public UserRole getOneByUserId(Integer id) {
        LambdaQueryWrapper<UserRole> urlqw = new LambdaQueryWrapper<>();
        urlqw.eq(UserRole::getUserId,id);
        UserRole userRole = userRoleDao.selectOne(urlqw);
        return userRole;
    }

    @Override
    public boolean updateRole(UserRole userRole) {
        LambdaUpdateWrapper<UserRole> urluw = new LambdaUpdateWrapper<>();
        urluw.eq(UserRole::getUserId,userRole.getUserId());
        int update = userRoleDao.update(userRole, urluw);
        if(update == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean removeRole(UserRole userRole) {
        LambdaQueryWrapper<UserRole> urlqw = new LambdaQueryWrapper<>();
        urlqw.eq(UserRole::getUserId,userRole.getUserId());
        int delete = userRoleDao.delete(urlqw);
        if(delete >= 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean addRole(UserRole userRole) {
        if(userRole == null){
            throw new BusinessException(500,null,"空参传递");
        }
        int insert = 0;
        userRole.setRoleName("管理员");
        try {
            insert = userRoleDao.insert(userRole);
        } catch (Exception e) {
            throw new BusinessException(500,"对应的用户id已经存在权限了");
        }
        if(insert == 1){
            return true;
        }
        return false;
    }
}
