package org.bohemia.webapi.service.impl;

import com.alibaba.fastjson.JSONObject;
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

    @Override
    public int addUser(JSONObject obj) {
        User newuser = new User();
        newuser.setUsername(obj.getString("username"));
        newuser.setPassword(obj.getString("password"));
        newuser.setIntroduction(obj.getString("introduction"));
        newuser.setName(obj.getString("name"));
        // 初始账号创建时，权限为普通，且状态为“待审核”，需要系统管理员审核通过后才能使用
        newuser.setRole("user");
        newuser.setStatus("verify");
        return userMapper.addUser(newuser);
    }

    @Override
    public int usernameCheck(String username) {
        return userMapper.usernameCheck(username);
    }
}
