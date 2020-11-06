package org.bohemia.webapi.entity;

import lombok.Data;

import java.util.List;

@Data
public class User {
    int id;
    String username;
    String password;
}
