package com.example.task.manager.controller;

import com.example.task.manager.dao.TicketDAO;
import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Components;
import com.example.task.manager.model.enums.Status;
import com.example.task.manager.model.enums.Type;
import com.example.task.manager.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    TicketDAO ticketDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    TicketService ticketService;

    @GetMapping("{ticketId}")
    public String getTicket(
            @PathVariable Long ticketId,
            Model model
    ) {

        Ticket ticket = ticketService.getTicketById(ticketId);
        List<Ticket> subTickets = ticketService.getAllStoryTickets(ticket);

        model.addAttribute("ticket", ticket);
        model.addAttribute("subTickets", subTickets);

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

        List<Ticket> subTickets = ticketService.getAllStoryTickets(ticket);

        model.addAttribute("ticket", ticket);
        model.addAttribute("subTickets", subTickets);

        return "ticket";
    }

    @PostMapping("/add")
    public String addExecutor(
            @RequestParam String username,
            @RequestParam Long ticketId
    ) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        List<User> oldExecutors = ticket.getExecutors();
        oldExecutors.add(userDAO.getUserByLogin(username));
        ticket.setExecutors(oldExecutors);

        ticketDAO.addExecutorToTicket(ticket);

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping("/bug")
    public String createBug(
            @AuthenticationPrincipal User user,
            @RequestParam String label,
            @RequestParam(required = false, defaultValue = "No info") String description,
            @RequestParam String executorLogin,
            @RequestParam String type,
            @RequestParam String status,
            @RequestParam(required = false, defaultValue = "None") String components,
            @RequestParam Long ticketId
    ) {

        ticketService.addTicket(user, label, description, executorLogin, type, status, components);

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping("/forward")
    public String forward(@RequestParam Long ticketId) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setStatus(ticket.getStatus().getNextStatus());
        ticketDAO.updateTicket(ticket);

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping("/reopen")
    public String reopen(@RequestParam Long ticketId) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setStatus(Status.ToDo);
        ticketDAO.updateTicket(ticket);

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping("/addSubTicket")
    public String addSubTicket(
            @RequestParam Long ticketId,
            @RequestParam Long subTicketId
    ) {

        Ticket subTicket = ticketDAO.getTicketById(subTicketId);
        subTicket.setStoryId(ticketId);
        ticketDAO.updateTicket(subTicket);

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping("/deleteSubTicket")
    public String deleteSubTicket(
            @RequestParam Long ticketId,
            @RequestParam Long subTicketId
    ) {

        Ticket subTicket = ticketDAO.getTicketById(subTicketId);
        subTicket.setStoryId(0L);
        ticketDAO.updateTicket(subTicket);

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping("/deleteExecutor")
    public String deleteExecutor(
            @RequestParam Long executorId,
            @RequestParam Long ticketId
    ) {

        ticketDAO.deleteExecutorFromTicket(ticketId, executorId);

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping("/createSubTicket")
    public String createSubTicket(
            @AuthenticationPrincipal User user,
            @RequestParam String label,
            @RequestParam(required = false, defaultValue = "No info") String description,
            @RequestParam String executorLogin,
            @RequestParam String type,
            @RequestParam String status,
            @RequestParam(required = false, defaultValue = "None") String components,
            @RequestParam Long ticketId
    ) {

        List<User> executors = new ArrayList<>();

        for (String login : executorLogin.split(" ")) {
            executors.add(userDAO.getUserByLogin(login));
        }

        if (label != null && !label.isEmpty() && user != null && !executors.isEmpty()) {

            List<Components> componentsList = Stream.of(components.split(" "))
                    .map(Components::valueOf)
                    .collect(Collectors.toList());

            Ticket ticket = new Ticket(label, description, user,
                    executors, Type.valueOf(type), Status.valueOf(status),
                    componentsList);

            ticket.setStoryId(ticketId);

            ticketDAO.addTicket(ticket);
        }

        return "redirect:/ticket/" + ticketId;
    }
}