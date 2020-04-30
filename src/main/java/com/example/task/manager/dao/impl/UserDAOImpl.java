package com.example.task.manager.dao.impl;

import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.dao.mapper.UserMapper;
import com.example.task.manager.dao.mapper.UserQuery;
import com.example.task.manager.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class UserDAOImpl implements UserDAO {

    private JdbcTemplate jdbcTemplate;
    private UserMapper userMapper;

    private final String SQL_FIND_USER_BY_LOGIN;
    private final String SQL_FIND_USER_BY_ID;
    private final String SQL_DELETE_USER;
    private final String SQL_UPDATE_USER;
    private final String SQL_GET_ALL;
    private final String SQL_INSERT_USER;
    private final String SQL_FIND_USER_BY_TICKET;

    @Autowired
    public UserDAOImpl(DataSource dataSource, UserMapper userMapper) throws IOException {

        jdbcTemplate = new JdbcTemplate(dataSource);
        this.userMapper = userMapper;

        UserQuery userQuery = new ObjectMapper(new YAMLFactory())
                .findAndRegisterModules()
                .readValue(new File("src/main/resources/userQuery.yml"), UserQuery.class);

        SQL_FIND_USER_BY_LOGIN = userQuery.getFindUserByLogin();
        SQL_FIND_USER_BY_ID = userQuery.getFindUserById();
        SQL_DELETE_USER = userQuery.getDeleteUser();
        SQL_UPDATE_USER = userQuery.getUpdateUser();
        SQL_GET_ALL = userQuery.getGetAllUsers();
        SQL_INSERT_USER = userQuery.getInsertUser();
        SQL_FIND_USER_BY_TICKET = userQuery.getFindUserByTicket();
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(SQL_GET_ALL, userMapper);
    }

    @Override
    public List<User> getUsersFromTicket(Long ticketId) {
        return jdbcTemplate.query(SQL_FIND_USER_BY_TICKET, new Object[] {ticketId}, userMapper);
    }

    @Override
    public User getUserById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_USER_BY_ID, new Object[] {id}, userMapper);
    }

    @Override
    public User getUserByLogin(String login) {
        return jdbcTemplate.queryForObject(SQL_FIND_USER_BY_LOGIN, new Object[] { login }, userMapper);
    }

    @Override
    public boolean deleteUser(User user) {
        return jdbcTemplate.update(SQL_DELETE_USER, user.getId()) > 0;
    }

    @Override
    public boolean updateUser(User user) {
        return jdbcTemplate.update(SQL_UPDATE_USER, user.getUsername(), user.getPassword(), user.getRole().toString(),
                user.isActive(), user.getId()) > 0;
    }

    @Override
    public boolean addUser(User user) {
        return jdbcTemplate.update(SQL_INSERT_USER, user.getUsername(), user.getPassword(),
                user.getRole().toString(), user.isActive()) > 0;
    }
}
