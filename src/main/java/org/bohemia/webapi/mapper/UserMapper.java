package org.bohemia.webapi.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.bohemia.webapi.entity.User;

public interface UserMapper {

    @Select("select * from user where username=#{user.username} and password=#{user.password}")
    User getLoginOne(@Param("user") User user);

    @Select("select * from user where id = #{id}")
    User findUserById(int id);

    @Select("select * from user where username = #{username}")
    User findUserByUsername(String username);

    @Insert("insert into user (name, username, password, role, introduction, avatar, status)"
            + "VALUES"
            + "(#{user.name}, #{user.username}, #{user.password}, #{user.role}, #{user.introduction}, #{user.avatar}, #{user.status})")
    int addUser(@Param("user") User user);

    @Update("update user set status=#{status} where username=#{username}")
    void changeUserStatus(String username, String status);

    @Select("select count(*) from user where username = #{username}")
    int usernameCheck(String username);
}
