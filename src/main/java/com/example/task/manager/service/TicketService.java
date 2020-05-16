package com.example.task.manager.service;

import com.example.task.manager.aop.UserNonBlockedCheck;
import com.example.task.manager.dao.TicketDAO;
import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Components;
import com.example.task.manager.model.enums.Status;
import com.example.task.manager.model.enums.Type;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class TicketService {

    private UserDAO userDAO;
    private TicketDAO ticketDAO;
    private AuthService authService;

    public TicketService(UserDAO userDAO, TicketDAO ticketDAO, AuthService authService) {
        this.userDAO = userDAO;
        this.ticketDAO = ticketDAO;
        this.authService = authService;
    }

    @UserNonBlockedCheck
    public Ticket getTicketById(Long id) {
        return ticketDAO.getTicketById(id);
    }

    @UserNonBlockedCheck
    public List<Ticket> getAllTickets() {
        return ticketDAO.getAllTickets();
    }

    @UserNonBlockedCheck
    public List<Ticket> getAllStoryTickets(Ticket story) {
        return ticketDAO.getAllStoryTickets(story);
    }

    @UserNonBlockedCheck
    public List<Ticket> getTicketsByFilter(String filterByType, String filterByCreator, String filterByTime) {
        return ticketDAO.getAllTickets().stream()
                .filter(t -> filterByType.isEmpty() || t.getType().toString().equals(filterByType))
                .filter(t -> filterByCreator.isEmpty() || t.getCreator().getUsername().equals(filterByCreator))
                .filter(t -> filterByTime.isEmpty() || t.getTime().toString().equals(filterByTime))
                .collect(Collectors.toList());
    }

    @UserNonBlockedCheck
    public boolean addTicket(User user, String label, String description, String executorLogin,
                          String type, String status, String components, Long storyId
    ) {

        List<User> executors = new ArrayList<>();

        for (String login : executorLogin.split(" ")) {
            if (authService.isExists(login)) {
                executors.add(userDAO.getUserByLogin(login));
            }
        }

        List<String> existingTypes = Stream.of(Type.values())
                .map(Type::toString)
                .collect(Collectors.toList());

        List<String> existingComponents = Stream.of(Components.values())
                .map(Components::toString)
                .collect(Collectors.toList());

        if (
                label != null && !label.isEmpty()
                && user != null && !executors.isEmpty()
                && existingComponents.containsAll(Arrays.asList(components.split(" ")))
                && existingTypes.contains(type)
        ) {

            List<Components> componentsList = Stream.of(components.split(" "))
                    .map(Components::valueOf)
                    .collect(Collectors.toList());

            Ticket ticket = new Ticket(label, description, user,
                    executors, Type.valueOf(type), Status.valueOf(status),
                    componentsList);

            ticket.setStoryId(storyId == 0L ? null : storyId);

            ticketDAO.addTicket(ticket);

            return true;
        }
        return false;
    }

    @UserNonBlockedCheck
    public void deleteTicket(Long ticketId) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);

        ticketDAO.deleteTicket(ticket);
    }

    @UserNonBlockedCheck
    public Ticket updateDescriptionAndGet(Long ticketId, String description) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setDescription(description);
        ticketDAO.updateTicket(ticket);

        return ticket;
    }

    @UserNonBlockedCheck
    public void addExecutorToTicket(Long ticketId, String username) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        List<User> executors = ticket.getExecutors();
        executors.add(userDAO.getUserByLogin(username));
        ticket.setExecutors(executors);

        ticketDAO.addExecutorToTicket(ticket);
    }

    @UserNonBlockedCheck
    public void updateToNextStatus(Long ticketId) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setStatus(ticket.getStatus().getNextStatus());
        ticketDAO.updateTicket(ticket);
    }

    @UserNonBlockedCheck
    public void updateToTodoStatus(Long ticketId) {

        Ticket ticket = ticketDAO.getTicketById(ticketId);
        ticket.setStatus(Status.ToDo);
        ticketDAO.updateTicket(ticket);
    }

    @UserNonBlockedCheck
    public void updateStoryId(Long ticketNumber, Long newStoryId) {

        Ticket subTicket = ticketDAO.getTicketByNumber(ticketNumber);
        subTicket.setStoryId(newStoryId);
        ticketDAO.updateTicket(subTicket);
    }

    @UserNonBlockedCheck
    public void deleteExecutorFromTicket(Long ticketId, Long executorId) {
        ticketDAO.deleteExecutorFromTicket(ticketId, executorId);
    }

    @UserNonBlockedCheck
    public boolean isTicketNumberAppropriate(String ticketNumber) {

        List<Long> numbers = ticketDAO.getAllTickets().stream()
                .map(Ticket::getNumber)
                .collect(Collectors.toList());

        return ticketNumber.matches("tbj-[0-9]*") && numbers.contains(Long.valueOf(ticketNumber.substring(4)));
    }
}
