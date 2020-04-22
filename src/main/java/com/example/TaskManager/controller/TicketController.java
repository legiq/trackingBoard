package com.example.TaskManager.controller;

import com.example.TaskManager.dao.impl.TicketDAOImpl;
import com.example.TaskManager.dao.impl.UserDAOImpl;
import com.example.TaskManager.model.Ticket;
import com.example.TaskManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    TicketDAOImpl ticketDAO;

    @Autowired
    UserDAOImpl userDAO;

    @GetMapping("{ticketId}")
    public String getTicket(
            @PathVariable Long ticketId,
            Model model
    ) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);

        model.addAttribute("ticket", ticket);

        return "ticket";
    }

    @PostMapping("{ticketId}")
    public String updateDescription(
            @PathVariable Long ticketId,
            @RequestParam String description,
            Model model
    ) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);

        ticket.setDescription(description);

        ticketDAO.updateTicket(ticket);

        model.addAttribute("ticket", ticket);

        return "ticket";
    }

    @PostMapping("/add")
    public String addExecutor(
            @RequestParam String username,
            @RequestParam Long ticketId
    ) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);

        User executor = userDAO.getUserByLogin(username);

        ticket.setExecutors(Collections.singletonList(executor));

        ticketDAO.updateTicket(ticket);

        return "redirect:/ticket/" + ticketId;
    }
}
