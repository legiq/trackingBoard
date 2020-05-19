package com.example.task.manager.controller;

import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Role;
import com.example.task.manager.service.AuthService;
import com.example.task.manager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private UserService userService;
    private AuthService authService;
    private static String registrationTemplate = "registration";
    private static String messageAttribute = "message";
    private static String redirectToLoginURL = "redirect:/login";

    public RegistrationController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @GetMapping("/registration")
    public String registration() {
        return registrationTemplate;
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {

        if(user.getRole().equals(Role.Admin)) {

            model.addAttribute(messageAttribute, "Can't register as admin");

            return registrationTemplate;
        } else if (authService.isExists(user.getUsername())) {

            model.addAttribute(messageAttribute, "User with such login already exists");

            return registrationTemplate;
        } else {

            user.setActive(true);
            userService.addUser(user);

            return redirectToLoginURL;
        }
    }
}
