package com.example.TaskManager.dao.impl;

import com.example.TaskManager.dao.UserDAO;
import com.example.TaskManager.model.User;
import com.example.TaskManager.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class UserDAOImpl implements UserDAO {

    private final String SQL_FIND_USER_BY_LOGIN = "select * from public.users where login = ?";

    private final String SQL_FIND_USER_BY_ID = "select * from users where id = ?";

    private final String SQL_DELETE_USER = "delete from users where id = ?";

    private final String SQL_UPDATE_USER = "update users " +
            "set login = ?, password = ?, role  = ?::user_role, active = ? where id = ?";

    private final String SQL_GET_ALL = "select * from users";

    private final String SQL_INSERT_USER = "insert into users (login, password, role, active) " +
            "values(?,?,?::user_role,?)";

    private final String SQL_FIND_USER_BY_TICKET = "SELECT * FROM users join tickets_executors " +
            "on users.id = tickets_executors.executor_id where tickets_executors.ticket_id = ?";

    private JdbcTemplate jdbcTemplate;
    private UserMapper userMapper;

    @Autowired
    public UserDAOImpl(DataSource dataSource, UserMapper userMapper) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.userMapper = userMapper;
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
