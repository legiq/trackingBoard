package com.example.TaskManager.controller;

import com.example.TaskManager.dao.impl.TicketDAOImpl;
import com.example.TaskManager.dao.impl.UserDAOImpl;
import com.example.TaskManager.dao.TicketDAO;
import com.example.TaskManager.dao.UserDAO;
import com.example.TaskManager.model.*;
import org.springframework.beans.factory.annotation.Autowired;
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
            @RequestParam String label,
            @RequestParam(required = false, defaultValue = "No info") String description,
            @RequestParam Long creator_id,
            @RequestParam Long executor_id,
            @RequestParam(required = false, defaultValue = "None") String type,
            @RequestParam(required = false, defaultValue = "None") String status,
            @RequestParam(required = false, defaultValue = "None") String components,
            Model model
    ) {

        User creator = userDAO.getUserById(creator_id);
        User executor = userDAO.getUserById(executor_id);

        if (label != null && !label.isEmpty() && creator != null && executor != null) {

            List<User> executors = new ArrayList<>();
            executors.add(executor);

            List<Components> componentsList = Stream.of(components.split(" "))
                    .map(Components::valueOf)
                    .collect(Collectors.toList());

            Ticket ticket = new Ticket(label, description, creator,
                    executors, Type.valueOf(type), Status.valueOf(status),
                    componentsList);

            ticketDAO.addTicket(ticket);
        }

        List<Ticket> tickets = ticketDAO.getAllTickets();

        model.addAttribute("tickets", tickets);

        return "main";
    }
}
