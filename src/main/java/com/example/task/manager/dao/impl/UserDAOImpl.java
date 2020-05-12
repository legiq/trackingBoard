package com.example.task.manager.dao.impl;

import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.dao.mapper.UserMapper;
import com.example.task.manager.dao.mapper.UserQuery;
import com.example.task.manager.model.Ticket;
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

    private final String sqlFindUserByLogin;
    private final String sqlFindUserById;
    private final String sqlDeleteUser;
    private final String sqlUpdateUser;
    private final String sqlGetAllUsers;
    private final String sqlInsertUser;
    private final String sqlFindUserByTicket;
    private final String sqlDeleteExecutor;
    private final String sqlDeleteAllUserTickets;
    private final String sqlDeleteTicketExecutors;

    @Autowired
    public UserDAOImpl(DataSource dataSource, UserMapper userMapper) throws IOException {

        jdbcTemplate = new JdbcTemplate(dataSource);
        this.userMapper = userMapper;

        UserQuery userQuery = new ObjectMapper(new YAMLFactory())
                .findAndRegisterModules()
                .readValue(new File("src/main/resources/userQuery.yml"), UserQuery.class);

        sqlFindUserByLogin = userQuery.getFindUserByLogin();
        sqlFindUserById = userQuery.getFindUserById();
        sqlDeleteUser = userQuery.getDeleteUser();
        sqlUpdateUser = userQuery.getUpdateUser();
        sqlGetAllUsers = userQuery.getGetAllUsers();
        sqlInsertUser = userQuery.getInsertUser();
        sqlFindUserByTicket = userQuery.getFindUserByTicket();
        sqlDeleteExecutor = userQuery.getDeleteExecutor();
        sqlDeleteAllUserTickets = userQuery.getDeleteAllUserTickets();
        sqlDeleteTicketExecutors = userQuery.getDeleteTicketExecutors();
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(sqlGetAllUsers, userMapper);
    }

    @Override
    public List<User> getUsersFromTicket(Long ticketId) {
        return jdbcTemplate.query(sqlFindUserByTicket, new Object[] {ticketId}, userMapper);
    }

    @Override
    public User getUserById(Long id) {
        return jdbcTemplate.queryForObject(sqlFindUserById, new Object[] {id}, userMapper);
    }

    @Override
    public User getUserByLogin(String login) {
        return jdbcTemplate.queryForObject(sqlFindUserByLogin, new Object[] { login }, userMapper);
    }

    @Override
    public boolean deleteUser(Long userId) {
        return jdbcTemplate.update(sqlDeleteUser, userId) > 0;
    }

    @Override
    public boolean updateUser(User user) {
        return jdbcTemplate.update(sqlUpdateUser, user.getUsername(), user.getPassword(), user.getRole().toString(),
                user.isActive(), user.getId()) > 0;
    }

    @Override
    public boolean addUser(User user) {
        return jdbcTemplate.update(sqlInsertUser, user.getUsername(), user.getPassword(),
                user.getRole().toString(), user.isActive()) > 0;
    }

    @Override
    public boolean deleteExecutor(Long userId) {
        return jdbcTemplate.update(sqlDeleteExecutor, userId) > 0;
    }

    @Override
    public boolean deleteAllUserTickets(Long userId) {
        return jdbcTemplate.update(sqlDeleteAllUserTickets, userId) > 0;
    }

    @Override
    public boolean deleteTicketsExecutors(List<Ticket> ticketByCreator) {

        for(Ticket ticket : ticketByCreator) {
            jdbcTemplate.update(sqlDeleteTicketExecutors, ticket.getId());
        }

        return true;
    }
}
