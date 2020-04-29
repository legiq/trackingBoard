package com.example.task.manager.dao;

import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;

import java.util.Date;
import java.util.List;

public interface TicketDAO {

    List<Ticket> getAllTickets();

    Ticket getTicketById(Long id);

    List<Ticket> getTicketByType(String type);

    List<Ticket> getTicketByTime(Date time);

    List<Ticket> getTicketByCreator(User user);

    List<Ticket> getAllStoryTickets(Ticket story);

    List<Ticket> getByAllFilters(User creator, String type, Date time);

    List<Ticket> getByCreatorAndTime(User creator, Date time);

    List<Ticket> getByCreatorAndType(User creator, String type);

    List<Ticket> getByTimeAndType(Date time, String type);

    boolean deleteTicket(Ticket ticket);

    boolean updateTicket(Ticket ticket);

    boolean addTicket(Ticket ticket);

    boolean addExecutorToTicket(Ticket ticket);

    boolean deleteExecutorFromTicket(Long ticketId, Long executorId);
}
