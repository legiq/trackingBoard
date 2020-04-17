package com.example.TaskManager.controller;

import com.example.TaskManager.DAOImpl.UserDAOImpl;
import com.example.TaskManager.model.Role;
import com.example.TaskManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private UserDAOImpl userDAO;

    @GetMapping("/main")
    public String show(Model model) {

        List<User> users = userDAO.getAllUsers();
        model.addAttribute("users", users);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @RequestParam String login,
            @RequestParam String password,
            @RequestParam String role,
            Model model
    ) {
        User user = new User(login, password, Role.valueOf(role));

        userDAO.addUser(user);
        List<User> users = userDAO.getAllUsers();

        model.addAttribute("users", users);

        return "main";
    }
}
