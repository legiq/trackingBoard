package com.example.TaskManager.controller;

import com.example.TaskManager.dao.impl.TicketDAOImpl;
import com.example.TaskManager.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    TicketDAOImpl ticketDAO;

    @GetMapping("{ticket_id}")
    public String getTicket(
            @PathVariable Long ticket_id,
            Model model
    ) {

        Ticket ticket = ticketDAO.getTicketById(ticket_id);

        model.addAttribute("ticket", ticket);

        return "ticket";
    }
}
