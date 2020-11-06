package org.bohemia.webapi.service;

import org.bohemia.webapi.entity.User;

public interface UserServiceApi {
    User getLoginOne(User user);

    User findUserById(int id);

    User findUserByUsername(String username);
}
