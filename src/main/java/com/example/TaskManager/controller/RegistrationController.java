package com.example.TaskManager.controller;

import com.example.TaskManager.dao.impl.UserDAOImpl;
import com.example.TaskManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @Autowired
    UserDAOImpl userDAO;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user) {

        user.setActive(true);

        userDAO.addUser(user);

        return "redirect:/login";
    }
}
