package com.example.task.manager.dao.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserQuery {

    private String findUserByLogin;
    private String findUserById;
    private String deleteUser;
    private String updateUser;
    private String getAllUsers;
    private String insertUser;
    private String findUserByTicket;

}
