package com.example.task.manager.controller;

import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;
import com.example.task.manager.service.AuthService;
import com.example.task.manager.service.TicketService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    private TicketService ticketService;
    private AuthService authService;
    private static final String redirectToTicketURL = "redirect:/ticket/";
    private static final String ticketAttribute = "ticket";
    private static final String ticketTemplate = "ticket";

    public TicketController(TicketService ticketService, AuthService authService) {
        this.ticketService = ticketService;
        this.authService = authService;
    }

    @GetMapping("{ticketId}")
    public String getTicket(
            @PathVariable Long ticketId,
            Model model
    ) {

        Ticket ticket = ticketService.getTicketById(ticketId);

        model.addAttribute(ticketAttribute, ticket);
        model.addAttribute("subTickets", ticketService.getAllStoryTickets(ticket));

        return ticketTemplate;
    }

    @PostMapping("{ticketId}")
    public String updateDescription(
            @PathVariable Long ticketId,
            @RequestParam String description,
            Model model
    ) {

        Ticket ticket = ticketService.updateDescriptionAndGet(ticketId, description);

        model.addAttribute(ticketAttribute, ticket);
        model.addAttribute("subTickets", ticketService.getAllStoryTickets(ticket));

        return ticketTemplate;
    }

    @PostMapping("/add")
    public String addExecutor(
            @RequestParam String username,
            @RequestParam Long ticketId
    ) {

        if (authService.isExists(username)) {
            ticketService.addExecutorToTicket(ticketId, username);
        }

        return redirectToTicketURL + ticketId;
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

        return redirectToTicketURL + ticketId;
    }

    @PostMapping("/forward")
    public String forward(@RequestParam Long ticketId) {

        ticketService.updateToNextStatus(ticketId);

        return redirectToTicketURL + ticketId;
    }

    @PostMapping("/reopen")
    public String reopen(@RequestParam Long ticketId) {

        ticketService.updateToTodoStatus(ticketId);

        return redirectToTicketURL + ticketId;
    }

    @PostMapping("/addSubTicket")
    public String addSubTicket(
            @RequestParam Long ticketId,
            @RequestParam String subTicketNumber
    ) {

        if (ticketService.isTicketNumberAppropriate(subTicketNumber)) {
            ticketService.updateStoryId(Long.valueOf(subTicketNumber.substring(4)), ticketId);
        }

        return redirectToTicketURL + ticketId;

    }

    @PostMapping("/deleteSubTicket")
    public String deleteSubTicket(
            @RequestParam Long ticketId,
            @RequestParam Long subTicketNumber
    ) {

        ticketService.updateStoryId(subTicketNumber, 0L);

        return redirectToTicketURL + ticketId;
    }

    @PostMapping("/deleteExecutor")
    public String deleteExecutor(
            @RequestParam Long executorId,
            @RequestParam Long ticketId
    ) {

        ticketService.deleteExecutorFromTicket(ticketId, executorId);

        return redirectToTicketURL + ticketId;
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

        return redirectToTicketURL + ticketId;
    }
}