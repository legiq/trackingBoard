package com.example.task.manager.service;

import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private UserDAO userDAO;

    @Autowired
    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void isBlocked() {

        User currentUser = userDAO.getUserById(
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

        if(!currentUser.isEnabled()) {
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        }
    }

    public boolean isExists(String username) {

        List<String> users = userDAO.getAllUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        return users.contains(username);
    }
}
