package com.example.task.manager.controller;

import com.example.task.manager.model.User;
import com.example.task.manager.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private TicketService ticketService;
    private static final String ticketsAttribute = "tickets";
    private static final String mainTemplate = "tickets";
    private static final String redirectToMainURL = "redirect:/main";

    @Autowired
    public MainController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/main")
    public String show(
            Model model
    ) {

        model.addAttribute(ticketsAttribute, ticketService.getAllTickets());

        return mainTemplate;
    }

    @PostMapping("/filter")
    public String showByFilter(
            @RequestParam(required = false, defaultValue = "") String filterByType,
            @RequestParam(required = false, defaultValue = "") String filterByTime,
            @RequestParam(required = false, defaultValue = "") String filterByCreator,
            Model model
    ) {

        model.addAttribute(ticketsAttribute,
                ticketService.getTicketsByFilter(filterByType, filterByCreator, filterByTime));

        model.addAttribute("filterByType", filterByType);
        model.addAttribute("filterByTime", filterByTime);
        model.addAttribute("filterByCreator", filterByCreator);

        return mainTemplate;
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

        ticketService.addTicket(user, label, description, executorLogin, type, status, components, 0L);

        model.addAttribute(ticketsAttribute, ticketService.getAllTickets());

        return mainTemplate;
    }

    @PostMapping("/delete")
    public String delete(
            @RequestParam Long ticketId
    ) {

        ticketService.deleteTicket(ticketId);

        return redirectToMainURL;
    }
}
