package com.example.task.manager.service;

import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean isExists(String username) {

        List<String> usersNames = userDAO.getAllUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        return usersNames.contains(username);
    }
}
