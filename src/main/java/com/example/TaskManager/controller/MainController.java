package com.example.TaskManager.controller;

import com.example.TaskManager.dao.TicketDAO;
import com.example.TaskManager.dao.UserDAO;
import com.example.TaskManager.dao.impl.TicketDAOImpl;
import com.example.TaskManager.dao.impl.UserDAOImpl;
import com.example.TaskManager.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public String show(
            @RequestParam(required = false, defaultValue = "") String filterByType,
            @RequestParam(required = false, defaultValue = "") String filterByTime,
            @RequestParam(required = false, defaultValue = "") String filterByCreator,
            Model model
    ) {

        List<Ticket> tickets;

        List<String> types = Stream.of(Type.values())
                .map(Type::toString)
                .collect(Collectors.toList());

        List<String> creators = userDAO.getAllUsers().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        if (types.contains(filterByType)) {
            tickets = ticketDAO.getTicketByType(Type.valueOf(filterByType));
        } else if (!filterByTime.isEmpty()) {
            try {
                tickets = ticketDAO.getTicketByTime(Date.valueOf(filterByTime));
            } catch (Exception ex) {
                tickets = Collections.emptyList();
            }
        } else if (creators.contains(filterByCreator)) {
            tickets = ticketDAO.getTicketByCreator(userDAO.getUserByLogin(filterByCreator));
        } else {
            tickets = ticketDAO.getAllTickets();
        }

        model.addAttribute("tickets", tickets);

        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String label,
            @RequestParam(required = false, defaultValue = "No info") String description,
            @RequestParam String executorLogin,
            @RequestParam String type,
            @RequestParam String status,
            @RequestParam(required = false, defaultValue = "None") String components,
            Model model
    ) {

        List<User> executors = new ArrayList<>();

        for(String login : executorLogin.split(" ")) {
            executors.add(userDAO.getUserByLogin(login));
        }

        if (label != null && !label.isEmpty() && user != null && !executors.isEmpty()) {

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
