package com.example.task.manager.service;

import com.example.task.manager.dao.TicketDAO;
import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private UserDAO userDAO;
    private TicketDAO ticketDAO;

    @Autowired
    public UserService(UserDAO userDAO, TicketDAO ticketDAO) {
        this.userDAO = userDAO;
        this.ticketDAO = ticketDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDAO.getUserByLogin(username);

        if (user.getUsername() == null || !user.isEnabled()) {
            throw new UsernameNotFoundException("User not authorized.");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());

        return new User(user.getId(), user.getUsername(), user.getPassword(),
                Role.valueOf(authority.getAuthority()), true);
    }

    public List<User> getAllUsers() {

        isBlocked();

        return userDAO.getAllUsers();
    }

    public boolean addUser(User user) {

        isBlocked();

        return userDAO.addUser(user);
    }

    public User getUserById(Long id) {

        isBlocked();

        return userDAO.getUserById(id);
    }

    public boolean updateUser(User user) {

        isBlocked();

        return userDAO.updateUser(user);
    }

    public boolean deleteUser(Long userId) {

        isBlocked();

        userDAO.deleteExecutor(userId);
        userDAO.deleteTicketsExecutors(ticketDAO.getTicketByCreator(userId));
        userDAO.deleteAllUserTickets(userId);
        userDAO.deleteUser(userId);

        return true;
    }

    public void disableUser(Long disableId) {

        isBlocked();

        User user = userDAO.getUserById(disableId);

        user.setActive(false);

        userDAO.updateUser(user);
    }

    public void enableUser(Long disableId) {

        isBlocked();

        User user = userDAO.getUserById(disableId);

        user.setActive(true);

        userDAO.updateUser(user);
    }

    public void isBlocked() {

        User currentUser = userDAO.getUserById(
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

        if(!currentUser.isEnabled()) {
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        }
    }

}
