package com.example.task.manager.service;

import com.example.task.manager.aop.UserNonBlockedCheck;
import com.example.task.manager.dao.TicketDAO;
import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private UserDAO userDAO;
    private TicketDAO ticketDAO;

    @Autowired
    public UserService(UserDAO userDAO, TicketDAO ticketDAO) {
        this.userDAO = userDAO;
        this.ticketDAO = ticketDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userDAO.getUserByLogin(username);

        if (user.getUsername() == null || !user.isEnabled()) {
            throw new UsernameNotFoundException("User not authorized.");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());

        return new User(user.getId(), user.getUsername(), user.getPassword(),
                Role.valueOf(authority.getAuthority()), true);
    }

    @UserNonBlockedCheck
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public void addUser(User user) {
        userDAO.addUser(user);
    }

    @UserNonBlockedCheck
    public User getUserById(Long id) {
        return userDAO.getUserById(id);
    }

    @UserNonBlockedCheck
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    @UserNonBlockedCheck
    public boolean deleteUser(Long userId) {

        userDAO.deleteExecutor(userId);
        userDAO.deleteTicketsExecutors(ticketDAO.getTicketByCreator(userId));
        userDAO.deleteAllUserTickets(userId);
        userDAO.deleteUser(userId);

        return true;
    }

    @UserNonBlockedCheck
    public void disableUser(Long disableId) {

        User user = userDAO.getUserById(disableId);
        user.setActive(false);

        userDAO.updateUser(user);
    }

    @UserNonBlockedCheck
    public void enableUser(Long disableId) {

        User user = userDAO.getUserById(disableId);
        user.setActive(true);

        userDAO.updateUser(user);
    }

    @UserNonBlockedCheck
    public List<User> getAllNonAdminUsers() {
        return getAllUsers().stream()
                .filter(u -> !u.getRole().equals(Role.Admin))
                .collect(Collectors.toList());
    }

}
