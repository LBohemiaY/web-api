package org.bohemia.webapi.service;

import com.alibaba.fastjson.JSONObject;
import org.bohemia.webapi.entity.User;

public interface UserServiceApi {
    User getLoginOne(User user);

    User findUserById(int id);

    User findUserByUsername(String username);

    int addUser(JSONObject obj);

    int usernameCheck(String username);
}
