package com.yiling.service;

import com.yiling.domain.User;

import java.util.List;

/**
 * @Author MisakiMikoto
 * @Date 2023/4/4 17:12
 */
public interface UserService {
    List<User> getAllUsers();

    boolean login(User user);

    boolean regiser(User user);

    boolean update(User user);

    boolean delete(User user);

    User getUserById(Integer id);

    User getUserByUsername(String username);



}
