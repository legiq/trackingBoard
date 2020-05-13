package com.example.task.manager.dao;

import com.example.task.manager.model.Ticket;

import java.util.List;

public interface TicketDAO {

    List<Ticket> getAllTickets();

    Ticket getTicketById(Long id);

    List<Ticket> getAllStoryTickets(Ticket story);

    List<Ticket> getTicketByCreator(Long userId);

    boolean deleteTicket(Ticket ticket);

    boolean updateTicket(Ticket ticket);

    boolean addTicket(Ticket ticket);

    boolean addExecutorToTicket(Ticket ticket);

    boolean deleteExecutorFromTicket(Long ticketId, Long executorId);
}
