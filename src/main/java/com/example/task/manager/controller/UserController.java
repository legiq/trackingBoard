package com.example.task.manager.controller;

import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Role;
import com.example.task.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('Admin')")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userList(Model model) {

        model.addAttribute("users", userService.getAllUsers());
        return "userList";
    }

    @GetMapping("{userId}")
    public String userForm(@PathVariable Long userId, Model model) {

        User user = userService.getUserById(userId);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("id", user.getId());
        model.addAttribute("userRole", user.getRole());
        model.addAttribute("roles", Role.values());

        return "userPage";
    }

    @PostMapping("{userId}")
    public String editUser(
            @PathVariable Long userId,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String roleRadio,
            Model model
    ) {

        User user = userService.getUserById(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(Role.valueOf(roleRadio));

        userService.updateUser(user);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("userRole", user.getRole());
        model.addAttribute("id", user.getId());
        model.addAttribute("roles", Role.values());

        return "userPage";
    }

    @PostMapping("/delete")
    public String deleteUser(
            @RequestParam Long deleteId
    ) {

        System.out.println(deleteId);

        userService.deleteUser(deleteId);

        return "redirect:/user";
    }
}
