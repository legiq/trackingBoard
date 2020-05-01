package com.example.task.manager.controller;

import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;
import com.example.task.manager.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    private TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("{ticketId}")
    public String getTicket(
            @PathVariable Long ticketId,
            Model model
    ) {

        Ticket ticket = ticketService.getTicketById(ticketId);

        model.addAttribute("ticket", ticket);
        model.addAttribute("subTickets", ticketService.getAllStoryTickets(ticket));

        return "ticket";
    }

    @PostMapping("{ticketId}")
    public String updateDescription(
            @PathVariable Long ticketId,
            @RequestParam String description,
            Model model
    ) {

        Ticket ticket = ticketService.updateDescriptionAndGet(ticketId, description);

        model.addAttribute("ticket", ticket);
        model.addAttribute("subTickets", ticketService.getAllStoryTickets(ticket));

        return "ticket";
    }

    @PostMapping("/add")
    public String addExecutor(
            @RequestParam String username,
            @RequestParam Long ticketId
    ) {

        ticketService.addExecutorToTicket(ticketId, username);

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

        ticketService.addTicket(user, label, description, executorLogin, type, status, components, 0L);

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping("/forward")
    public String forward(@RequestParam Long ticketId) {

        ticketService.updateToNextStatus(ticketId);

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping("/reopen")
    public String reopen(@RequestParam Long ticketId) {

        ticketService.updateToTodoStatus(ticketId);

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping("/addSubTicket")
    public String addSubTicket(
            @RequestParam Long ticketId,
            @RequestParam Long subTicketId
    ) {

        ticketService.updateStoryId(subTicketId, ticketId);

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping("/deleteSubTicket")
    public String deleteSubTicket(
            @RequestParam Long ticketId,
            @RequestParam Long subTicketId
    ) {

        ticketService.updateStoryId(subTicketId, 0L);

        return "redirect:/ticket/" + ticketId;
    }

    @PostMapping("/deleteExecutor")
    public String deleteExecutor(
            @RequestParam Long executorId,
            @RequestParam Long ticketId
    ) {

        ticketService.deleteExecutorFromTicket(ticketId, executorId);

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

        ticketService.addTicket(user, label, description, executorLogin, type, status, components, ticketId);

        return "redirect:/ticket/" + ticketId;
    }
}