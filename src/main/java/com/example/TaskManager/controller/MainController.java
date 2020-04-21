package com.example.TaskManager.controller;

import com.example.TaskManager.dao.impl.TicketDAOImpl;
import com.example.TaskManager.dao.impl.UserDAOImpl;
import com.example.TaskManager.dao.TicketDAO;
import com.example.TaskManager.dao.UserDAO;
import com.example.TaskManager.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MainController {

    private UserDAO userDAO;
    private TicketDAO ticketDAO;

    @Autowired
    public MainController(UserDAOImpl userDAO, TicketDAOImpl ticketDAO) {
        this.userDAO = userDAO;
        this.ticketDAO = ticketDAO;
    }

    @GetMapping("/main")
    public String show(Model model) {

        List<Ticket> tickets = ticketDAO.getAllTickets();
        model.addAttribute("tickets", tickets);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String label,
            @RequestParam(required = false, defaultValue = "No info") String description,
            @RequestParam String executor_login,
            @RequestParam String type,
            @RequestParam String status,
            @RequestParam(required = false, defaultValue = "None") String components,
            Model model
    ) {

        User executor = userDAO.getUserByLogin(executor_login);

        if (label != null && !label.isEmpty() && user != null && executor != null) {

            List<User> executors = new ArrayList<>();
            executors.add(executor);

            List<Components> componentsList = Stream.of(components.split(" "))
                    .map(Components::valueOf)
                    .collect(Collectors.toList());

            Ticket ticket = new Ticket(label, description, user,
                    executors, Type.valueOf(type), Status.valueOf(status),
                    componentsList);

            ticketDAO.addTicket(ticket);
        }

        List<Ticket> tickets = ticketDAO.getAllTickets();

        model.addAttribute("tickets", tickets);

        return "main";
    }

    @PostMapping("/delete")
    public String delete(
            @RequestParam Long ticket_id
    ) {

        Ticket ticket = ticketDAO.getTicketById(ticket_id);

        ticketDAO.deleteTicket(ticket);

        return "redirect:/main";
    }
}
