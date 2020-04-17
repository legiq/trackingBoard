package com.example.TaskManager.DAOImpl;

import com.example.TaskManager.DAO.UserDAO;
import com.example.TaskManager.model.User;
import com.example.TaskManager.model.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class UserDAOImpl implements UserDAO {

    JdbcTemplate jdbcTemplate;

    private final String SQL_FIND_USER = "select * from users where id = ?";
    private final String SQL_DELETE_USER = "delete from users where id = ?";
    private final String SQL_UPDATE_USER = "update users set login = ?, password = ?, role  = ? where id = ?";
    private final String SQL_GET_ALL = "select * from users";
    private final String SQL_INSERT_USER = "insert into users (login, password, role) values(?,?,?)";

    @Autowired
    public UserDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User getUserById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_USER, new Object[] { id }, new UserMapper());
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(SQL_GET_ALL, new UserMapper());
    }

    @Override
    public boolean deleteUser(User user) {
        return jdbcTemplate.update(SQL_DELETE_USER, user.getId()) > 0;
    }

    @Override
    public boolean updateUser(User user) {
        return jdbcTemplate.update(SQL_UPDATE_USER, user.getLogin(), user.getPassword(), user.getRole(),
                user.getId()) > 0;
    }

    @Override
    public boolean addUser(User user) {
        return jdbcTemplate.update(SQL_INSERT_USER, user.getLogin(), user.getPassword(),
                user.getRole().toString()) > 0;
    }
}
