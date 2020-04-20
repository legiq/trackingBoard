package com.example.TaskManager.controller;

import com.example.TaskManager.DAO.DAOImpl.UserDAOImpl;
import com.example.TaskManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    UserDAOImpl userDAO;

    @GetMapping("/")
    public String home(Model model) {

        List<User> users = userDAO.getAllUsers();

        model.addAttribute("users", users);

        return "home";
    }
}
