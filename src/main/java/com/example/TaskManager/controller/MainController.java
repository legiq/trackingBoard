package com.example.TaskManager.controller;

import com.example.TaskManager.model.Ticket;
import com.example.TaskManager.model.User;
import com.example.TaskManager.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    private TicketService ticketService;

    private Map<String, List<Ticket>> filtered = new HashMap<>();
    private Map<String, String> filters = new HashMap<>();

    @Autowired
    public MainController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/main")
    public String show(
            Model model
    ) {

        if(filtered.isEmpty() && filters.isEmpty()) {
            model.addAttribute("tickets",
                    ticketService.getAllTickets());
        } else {
            model.addAttribute("tickets", filtered.get("filtered"));
            model.addAttribute("filterByType", filters.get("filterByType"));
            model.addAttribute("filterByTime", filters.get("filterByTime"));
            model.addAttribute("filterByCreator", filters.get("filterByCreator"));
            filtered.clear();
            filters.clear();
        }

        return "main";
    }

    @PostMapping("/filter")
    public String showByFilter(
            @RequestParam(required = false, defaultValue = "") String filterByType,
            @RequestParam(required = false, defaultValue = "") String filterByTime,
            @RequestParam(required = false, defaultValue = "") String filterByCreator
    ) {

        filtered.put("filtered", ticketService.getTicketsByFilter(filterByType, filterByCreator, filterByTime));

        filters.put("filterByType", filterByType);
        filters.put("filterByTime", filterByTime);
        filters.put("filterByCreator", filterByCreator);

        return "redirect:/main";
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

        model.addAttribute("tickets",
                ticketService.addAndShowTickets(user, label, description, executorLogin, type, status, components));

        return "main";
    }

    @PostMapping("/delete")
    public String delete(
            @RequestParam Long ticketId
    ) {

        ticketService.deleteTicket(ticketId);

        return "redirect:/main";
    }
}
