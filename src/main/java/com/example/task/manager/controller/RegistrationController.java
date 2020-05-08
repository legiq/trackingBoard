package com.example.task.manager.controller;

import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Role;
import com.example.task.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {

        if(user.getRole().equals(Role.Admin)) {

            model.addAttribute("message", "Can't register as admin");

            return "registration";
        } else {

            user.setActive(true);
            userService.addUser(user);

            return "redirect:/login";
        }
    }
}
