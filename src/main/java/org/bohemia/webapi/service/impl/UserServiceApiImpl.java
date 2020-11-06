package org.bohemia.webapi.service.impl;

import org.bohemia.webapi.entity.User;
import org.bohemia.webapi.mapper.UserMapper;
import org.bohemia.webapi.service.UserServiceApi;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@MapperScan("org.bohemia.webapi.mapper")
@Service
public class UserServiceApiImpl implements UserServiceApi {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User getLoginOne(User user) {
        return userMapper.getLoginOne(user);
    }

    @Override
    public User findUserById(int id) {
        return userMapper.findUserById(id);
    }

    @Override
    public User findUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }
}
