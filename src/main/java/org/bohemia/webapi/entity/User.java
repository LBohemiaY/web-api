package org.bohemia.webapi.entity;

import lombok.Data;

import java.util.List;

@Data
public class User {
    int id;
    String name;
    String username;
    String password;
    String role;
    String introduction;
    String avatar;
    String status;
}
