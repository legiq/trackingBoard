package com.example.task.manager.dao;

import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.enums.Type;
import com.example.task.manager.model.User;

import java.util.Date;
import java.util.List;

public interface TicketDAO {

    List<Ticket> getAllTickets();

    Ticket getTicketById(Long id);

    List<Ticket> getTicketByType(Type type);

    List<Ticket> getTicketByTime(Date time);

    List<Ticket> getTicketByCreator(User user);

    List<Ticket> getAllStoryTickets(Ticket story);

    boolean deleteTicket(Ticket ticket);

    boolean updateTicket(Ticket ticket);

    boolean addTicket(Ticket ticket);

    boolean addExecutorToTicket(Ticket ticket);
}
