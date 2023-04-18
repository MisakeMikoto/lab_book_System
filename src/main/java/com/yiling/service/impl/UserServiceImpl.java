package com.yiling.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yiling.controller.exception.BusinessException;
import com.yiling.dao.ReservationDao;
import com.yiling.dao.UserDao;
import com.yiling.dao.UserRoleDao;
import com.yiling.domain.Reservation;
import com.yiling.domain.User;
import com.yiling.domain.UserRole;
import com.yiling.service.ReservationService;
import com.yiling.service.UserService;
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
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public List<User> getAllUsers() {
        return userDao.selectAll();
    }


    @Override
    public boolean login(User user) {
        if(user == null){
            return false;
        }
        String username = user.getUsername();
        String password = user.getPassword();
        if(username == null || password == null){
            return false;
        }
        LambdaQueryWrapper<User> ulqw = new LambdaQueryWrapper<>();
        ulqw.eq(User::getUsername,username);
        ulqw.eq(User::getPassword,password);
        User user1 = userDao.selectOne(ulqw);
        if(user1 != null ){
            return true;
        }
        return false;
    }
    @Override
    public boolean regiser(User user) {
        try {
            int insert = userDao.insert(user);
            if(insert == 1){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        if(user == null){
            return false;
        }
        LambdaUpdateWrapper<User> luw = new LambdaUpdateWrapper<>();
        luw.eq(user.getUserId() != null,User::getUserId,user.getUserId());
        luw.eq(user.getUsername()!=null,User::getUsername,user.getUsername());
        try {
            int update = userDao.update(user, luw);
            if(update == 1){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(User user) {
        if(user == null){
            throw new BusinessException(500,"空参传递");
        }
        LambdaQueryWrapper<User> ulqw = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<UserRole> uilqw = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Reservation> ufhlqw = new LambdaQueryWrapper<>();
        ulqw.eq(User::getUserId, user.getUserId());
        uilqw.eq(UserRole::getUserId,user.getUserId());
        ufhlqw.eq(Reservation::getUserId,user.getUserId());
        try {
            userRoleDao.delete(uilqw);
            reservationDao.delete(ufhlqw);
            int delete = userDao.delete(ulqw);
            if(delete == 1){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User getUserById(Integer id) {
        LambdaQueryWrapper<User> ulqw = new LambdaQueryWrapper<>();
        ulqw.eq(User::getUserId,id);
        User user = userDao.selectOne(ulqw);
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> ulqw = new LambdaQueryWrapper<>();
        ulqw.eq(User::getUsername,username);
        User user = userDao.selectOne(ulqw);
        return user;
    }
}
