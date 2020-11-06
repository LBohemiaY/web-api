package org.bohemia.webapi.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.bohemia.webapi.entity.User;

public interface UserMapper {

    @Select("select * from user where username=#{user.username} and password=#{user.password}")
    public User getLoginOne(@Param("user") User user);

    @Select("select * from user where id = #{id}")
    public User findUserById(int id);

    @Select("select * from user where username = #{username}")
    public User findUserByUsername(String username);
}
