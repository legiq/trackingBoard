package com.example.TaskManager.dao;

import com.example.TaskManager.model.Ticket;
import com.example.TaskManager.model.enums.Type;
import com.example.TaskManager.model.User;

import java.util.Date;
import java.util.List;

public interface TicketDAO {

    List<Ticket> getAllTickets();

    Ticket getTicketById(Long id);

    List<Ticket> getTicketByType(Type type);

    List<Ticket> getTicketByTime(Date time);

    List<Ticket> getTicketByCreator(User user);

    boolean deleteTicket(Ticket ticket);

    boolean updateTicket(Ticket ticket);

    boolean addTicket(Ticket ticket);

    boolean addExecutorToTicket(Ticket ticket);
}
