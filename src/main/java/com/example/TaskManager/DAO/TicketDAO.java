package com.example.TaskManager.DAO;

import com.example.TaskManager.model.Ticket;
import com.example.TaskManager.model.Type;
import com.example.TaskManager.model.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TicketDAO {

    List<Ticket> getAllTickets();

    Ticket getTicketByType(Type type);

    Ticket getTicketByTime(Date time);

    Ticket getTicketByCreator(User user);

    boolean deleteTicket(Ticket ticket);

    boolean updateTicket(Ticket ticket);

    boolean addTicket(Ticket ticket);
}
