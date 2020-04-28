package com.example.task.manager.controller;

import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private UserDAO userDAO;

    @Autowired
    public HomeController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/")
    public String home(Model model) {

        List<User> users = userDAO.getAllUsers();

        model.addAttribute("users", users);

        return "home";
    }
}
