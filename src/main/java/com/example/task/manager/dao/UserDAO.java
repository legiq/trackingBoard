package com.example.task.manager.dao;

import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;

import java.util.List;

public interface UserDAO {

    User getUserById(Long id);

    List<User> getUsersFromTicket(Long executorId);

    User getUserByLogin(String login);

    List<User> getAllUsers();

    boolean deleteUser(Long userId);

    boolean updateUser(User user);

    boolean addUser(User user);

    boolean deleteExecutor(Long userId);

    boolean deleteAllUserTickets(Long userId);

    boolean deleteTicketsExecutors(List<Ticket> ticketByCreator);
}
