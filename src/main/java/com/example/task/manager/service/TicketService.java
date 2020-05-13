package com.example.task.manager.service;

import com.example.task.manager.aop.NonBlockedCheck;
import com.example.task.manager.dao.TicketDAO;
import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Components;
import com.example.task.manager.model.enums.Status;
import com.example.task.manager.model.enums.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class TicketService {

    private UserDAO userDAO;
    private TicketDAO ticketDAO;

    @Autowired
    public TicketService(UserDAO userDAO, TicketDAO ticketDAO) {
        this.userDAO = userDAO;
        this.ticketDAO = ticketDAO;
    }

    @NonBlockedCheck
    public Ticket getTicketById(Long id) {
        return ticketDAO.getTicketById(id);
    }

    @NonBlockedCheck
    public List<Ticket> getAllTickets() {
        return ticketDAO.getAllTickets();
    }

    @NonBlockedCheck
    public List<Ticket> getAllStoryTickets(Ticket story) {
        return ticketDAO.getAllStoryTickets(story);
    }

    @NonBlockedCheck
    public List<Ticket> getTicketsByFilter(String filterByType, String filterByCreator, String filterByTime) {
        return ticketDAO.getAllTickets().stream()
                .filter(t -> filterByType.isEmpty() || t.getType().toString().equals(filterByType))
                .filter(t -> filterByCreator.isEmpty() || t.getCreator().getUsername().equals(filterByCreator))
                .filter(t -> filterByTime.isEmpty() || t.getTime().toString().equals(filterByTime))
                .collect(Collectors.toList());
    }

    @NonBlockedCheck
    public void addTicket(User user, String label, String description, String executorLogin,
                          String type, String status, String components, Long storyId
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

            ticket.setStoryId(storyId == 0L ? null : storyId);

            ticketDAO.addTicket(ticket);
        }
    }

    @NonBlockedCheck
    public void deleteTicket(Long ticketId) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);

        ticketDAO.deleteTicket(ticket);
    }

    @NonBlockedCheck
    public Ticket updateDescriptionAndGet(Long ticketId, String description) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setDescription(description);
        ticketDAO.updateTicket(ticket);

        return ticket;
    }

    @NonBlockedCheck
    public void addExecutorToTicket(Long ticketId, String username) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        List<User> executors = ticket.getExecutors();
        executors.add(userDAO.getUserByLogin(username));
        ticket.setExecutors(executors);

        ticketDAO.addExecutorToTicket(ticket);
    }

    @NonBlockedCheck
    public void updateToNextStatus(Long ticketId) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setStatus(ticket.getStatus().getNextStatus());
        ticketDAO.updateTicket(ticket);
    }

    @NonBlockedCheck
    public void updateToTodoStatus(Long ticketId) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setStatus(Status.ToDo);
        ticketDAO.updateTicket(ticket);
    }

    @NonBlockedCheck
    public void updateStoryId(Long ticketId, Long newStoryId) {

        Ticket subTicket = ticketDAO.getTicketById(ticketId);
        subTicket.setStoryId(newStoryId);
        ticketDAO.updateTicket(subTicket);
    }

    @NonBlockedCheck
    public void deleteExecutorFromTicket(Long ticketId, Long executorId) {
        ticketDAO.deleteExecutorFromTicket(ticketId, executorId);
    }
}
