package com.example.task.manager.dao.mapper;

import com.example.task.manager.model.enums.Role;
import com.example.task.manager.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(Role.valueOf(resultSet.getString("role")));
        user.setActive(resultSet.getBoolean("active"));
        return user;
    }
}
