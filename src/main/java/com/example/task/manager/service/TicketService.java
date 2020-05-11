package com.example.task.manager.service;

import com.example.task.manager.dao.TicketDAO;
import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Components;
import com.example.task.manager.model.enums.Status;
import com.example.task.manager.model.enums.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TicketService {

    private UserDAO userDAO;
    private TicketDAO ticketDAO;
    private AuthService authService;

    @Autowired
    public TicketService(UserDAO userDAO, TicketDAO ticketDAO, AuthService authService) {
        this.userDAO = userDAO;
        this.ticketDAO = ticketDAO;
        this.authService = authService;
    }

    public Ticket getTicketById(Long id) {

        authService.isBlocked();

        return ticketDAO.getTicketById(id);
    }

    public List<Ticket> getAllTickets() {

        authService.isBlocked();

        return ticketDAO.getAllTickets();
    }

    public List<Ticket> getAllStoryTickets(Ticket story) {

        authService.isBlocked();

        return ticketDAO.getAllStoryTickets(story);
    }

    public List<Ticket> getTicketsByFilter(String filterByType, String filterByCreator, String filterByTime) {

        authService.isBlocked();

        return ticketDAO.getAllTickets().stream()
                .filter(t -> filterByType.isEmpty() || t.getType().toString().equals(filterByType))
                .filter(t -> filterByCreator.isEmpty() || t.getCreator().getUsername().equals(filterByCreator))
                .filter(t -> filterByTime.isEmpty() || t.getTime().toString().equals(filterByTime))
                .collect(Collectors.toList());
    }

    public void addTicket(User user, String label, String description, String executorLogin,
                          String type, String status, String components, Long storyId
    ) {

        authService.isBlocked();

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

            ticket.setStoryId(storyId == 0L ? null : storyId);

            ticketDAO.addTicket(ticket);
        }
    }

    public void deleteTicket(Long ticketId) {

        authService.isBlocked();

        Ticket ticket = ticketDAO.getTicketById(ticketId);

        ticketDAO.deleteTicket(ticket);
    }

    public Ticket updateDescriptionAndGet(Long ticketId, String description) {

        authService.isBlocked();

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setDescription(description);
        ticketDAO.updateTicket(ticket);

        return ticket;
    }

    public void addExecutorToTicket(Long ticketId, String username) {

        authService.isBlocked();

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        List<User> executors = ticket.getExecutors();
        executors.add(userDAO.getUserByLogin(username));
        ticket.setExecutors(executors);

        ticketDAO.addExecutorToTicket(ticket);
    }

    public void updateToNextStatus(Long ticketId) {

        authService.isBlocked();

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setStatus(ticket.getStatus().getNextStatus());
        ticketDAO.updateTicket(ticket);
    }

    public void updateToTodoStatus(Long ticketId) {

        authService.isBlocked();

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setStatus(Status.ToDo);
        ticketDAO.updateTicket(ticket);
    }

    public void updateStoryId(Long ticketId, Long newStoryId) {

        authService.isBlocked();

        Ticket subTicket = ticketDAO.getTicketById(ticketId);
        subTicket.setStoryId(newStoryId);
        ticketDAO.updateTicket(subTicket);
    }

    public void deleteExecutorFromTicket(Long ticketId, Long executorId) {

        authService.isBlocked();

        ticketDAO.deleteExecutorFromTicket(ticketId, executorId);
    }
}
