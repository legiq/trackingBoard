package com.example.task.manager.dao;

import com.example.task.manager.model.User;

import java.util.List;

public interface UserDAO {

    User getUserById(Long id);

    List<User> getUsersFromTicket(Long executor_id);

    User getUserByLogin(String login);

    List<User> getAllUsers();

    boolean deleteUser(User user);

    boolean updateUser(User user);

    boolean addUser(User user);
}
