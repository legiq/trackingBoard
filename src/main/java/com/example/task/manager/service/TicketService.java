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
    public TicketService(UserDAO userDAO, TicketDAO ticketDAO) {
        this.userDAO = userDAO;
        this.ticketDAO = ticketDAO;
    }

    public Ticket getTicketById(Long id) {
        return ticketDAO.getTicketById(id);
    }

    public List<Ticket> getAllTickets() {
        return ticketDAO.getAllTickets();
    }

    public List<Ticket> getAllStoryTickets(Ticket story) {
        return ticketDAO.getAllStoryTickets(story);
    }

    public List<Ticket> getTicketsByFilter(String filterByType, String filterByCreator, String filterByTime) {

        List<Ticket> tickets;

        if (!filterByCreator.isEmpty() || !filterByType.isEmpty() || !filterByTime.isEmpty()) {

            List<String> types = Stream.of(Type.values())
                    .map(Type::toString)
                    .collect(Collectors.toList());

            List<String> creators = userDAO.getAllUsers().stream()
                    .map(User::getUsername)
                    .collect(Collectors.toList());

            if (creators.contains(filterByCreator) && types.contains(filterByType) && !filterByTime.isEmpty()) {

                try {
                    tickets = ticketDAO.getByAllFilters(userDAO.getUserByLogin(filterByCreator),
                            filterByType,
                            Date.valueOf(filterByTime));
                } catch (Exception ex) {
                    tickets = Collections.emptyList();
                }

            } else if (creators.contains(filterByCreator) && !filterByTime.isEmpty() && filterByType.isEmpty()) {

                try {
                    tickets = ticketDAO.getByCreatorAndTime(userDAO.getUserByLogin(filterByCreator),
                            Date.valueOf(filterByTime));
                } catch (Exception ex) {
                    tickets = Collections.emptyList();
                }

            } else if (creators.contains(filterByCreator) && types.contains(filterByType) && filterByTime.isEmpty()) {

                tickets = ticketDAO.getByCreatorAndType(userDAO.getUserByLogin(filterByCreator), filterByType);

            } else if (!filterByTime.isEmpty() && types.contains(filterByType) && filterByCreator.isEmpty()) {

                try {
                    tickets = ticketDAO.getByTimeAndType(Date.valueOf(filterByTime), filterByType);
                } catch (Exception ex) {
                    tickets = Collections.emptyList();
                }

            } else if (creators.contains(filterByCreator) && filterByTime.isEmpty() && filterByType.isEmpty()) {

                tickets = ticketDAO.getTicketByCreator(userDAO.getUserByLogin(filterByCreator));

            } else if (!filterByTime.isEmpty() && filterByCreator.isEmpty() && filterByType.isEmpty()) {

                try {
                    tickets = ticketDAO.getTicketByTime(Date.valueOf(filterByTime));
                } catch (Exception ex) {
                    tickets = Collections.emptyList();
                }

            } else if (types.contains(filterByType) && filterByCreator.isEmpty() && filterByTime.isEmpty()) {

                tickets = ticketDAO.getTicketByType(filterByType);

            } else {
                tickets = Collections.emptyList();
            }

        } else {

            tickets = ticketDAO.getAllTickets();

        }

        return tickets;
    }

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

    public void deleteTicket(Long ticketId) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);

        ticketDAO.deleteTicket(ticket);
    }

    public Ticket updateDescriptionAndGet(Long ticketId, String description) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setDescription(description);
        ticketDAO.updateTicket(ticket);

        return ticket;
    }

    public void addExecutorToTicket(Long ticketId, String username) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        List<User> executors = ticket.getExecutors();
        executors.add(userDAO.getUserByLogin(username));
        ticket.setExecutors(executors);

        ticketDAO.addExecutorToTicket(ticket);
    }

    public void updateToNextStatus(Long ticketId) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setStatus(ticket.getStatus().getNextStatus());
        ticketDAO.updateTicket(ticket);
    }

    public void updateToTodoStatus(Long ticketId) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setStatus(Status.ToDo);
        ticketDAO.updateTicket(ticket);
    }

    public void updateStoryId(Long ticketId, Long newStoryId) {

        Ticket subTicket = ticketDAO.getTicketById(ticketId);
        subTicket.setStoryId(newStoryId);
        ticketDAO.updateTicket(subTicket);
    }

    public void deleteExecutorFromTicket(Long ticketId, Long executorId) {
        ticketDAO.deleteExecutorFromTicket(ticketId, executorId);
    }
}
