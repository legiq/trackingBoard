package com.example.TaskManager.service;

import com.example.TaskManager.dao.TicketDAO;
import com.example.TaskManager.dao.UserDAO;
import com.example.TaskManager.model.Ticket;
import com.example.TaskManager.model.User;
import com.example.TaskManager.model.enums.Components;
import com.example.TaskManager.model.enums.Status;
import com.example.TaskManager.model.enums.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TicketService {

    private UserDAO userDAO;
    private TicketDAO ticketDAO;

    @Autowired
    public TicketService (UserDAO userDAO, TicketDAO ticketDAO) {
        this.userDAO = userDAO;
        this.ticketDAO = ticketDAO;
    }

    public List<Ticket> getAllTickets() {
        return ticketDAO.getAllTickets();
    }

    public List<Ticket> getTicketsByFilter(String filterByType, String filterByCreator, String filterByTime) {

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

        return tickets;
    }

    public List<Ticket> addAndShowTickets(User user, String label, String description, String executorLogin,
                                          String type, String status, String components
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

        return ticketDAO.getAllTickets();
    }

    public void deleteTicket(Long ticketId) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);

        ticketDAO.deleteTicket(ticket);
    }
}
