package com.example.task.manager.dao.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuery {

    private String findUserByLogin;
    private String findUserById;
    private String deleteUser;
    private String updateUser;
    private String getAllUsers;
    private String insertUser;
    private String findUserByTicket;
}
