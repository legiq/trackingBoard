package com.example.task.manager.unit;

import com.example.task.manager.dao.TicketDAO;
import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Role;
import com.example.task.manager.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private TicketDAO ticketDAO;

    @InjectMocks
    private UserService userService;

    @Test
    public void addUserTest() {

        User user = new User();

        userService.addUser(user);

        assertFalse(user.isActive());
        Mockito.verify(userDAO, Mockito.times(1)).addUser(user);
    }

    @Test
    public void getAllUsersTest() {

        Mockito.when(userDAO.getAllUsers()).thenReturn(createTestList());

        List<User> actual = userService.getAllUsers();

        assertEquals("password", actual.get(0).getPassword());
        assertEquals("neoleg", actual.get(1).getUsername());
        Mockito.verify(userDAO, Mockito.times(1)).getAllUsers();
    }

    @Test
    public void getUserByIdTest() {

        Mockito.when(userDAO.getUserById(1L)).thenReturn(createTestList().get(0));

        User actual = userService.getUserById(1L);

        assertEquals(1L, actual.getId());
        Mockito.verify(userDAO, Mockito.times(1)).getUserById(1L);
    }

    @Test
    public void updateUserTest() {

        User user = createTestList().get(0);
        userService.updateUser(user);

        Mockito.verify(userDAO, Mockito.times(1)).updateUser(user);
    }

    @Test
    public void deleteUserTest() {

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());

        Mockito.when(ticketDAO.getTicketByCreator(1L)).thenReturn(tickets);

        userService.deleteUser(1L);

        Mockito.verify(userDAO, Mockito.times(1)).deleteExecutor(1L);
        Mockito.verify(userDAO, Mockito.times(1))
                .deleteTicketsExecutors(ticketDAO.getTicketByCreator(1L));
        Mockito.verify(userDAO, Mockito.times(1)).deleteAllUserTickets(1L);
        Mockito.verify(userDAO, Mockito.times(1)).deleteUser(1L);
        Mockito.verify(ticketDAO, Mockito.times(2)).getTicketByCreator(1L);
    }

    @Test
    public void disableOrEnableUserTest() {

        User user = createTestList().get(0);

        Mockito.when(userDAO.getUserById(1L)).thenReturn(user);

        userService.disableUser(1L);

        assertEquals(1L, user.getId());
        Mockito.verify(userDAO, Mockito.times(1)).getUserById(1L);
        Mockito.verify(userDAO, Mockito.times(1)).updateUser(user);
    }

    @Test
    public void getAllNonAdminUsersTest() {

        Mockito.when(userDAO.getAllUsers()).thenReturn(createTestList());

        List<User> actual = userService.getAllNonAdminUsers();

        assertEquals(2, actual.size());
        assertEquals(Role.Developer, actual.get(0).getRole());
        assertEquals(Role.QA, actual.get(1).getRole());
        Mockito.verify(userDAO, Mockito.times(1)).getAllUsers();
    }

    private List<User> createTestList() {
        List<User> list = new ArrayList<>();
        list.add(new User(1L, "oleg", "password", Role.Developer, true));
        list.add(new User(2L, "neoleg", "drowssap", Role.QA, true));
        list.add(new User(3L, "admin", "drowssap", Role.Admin, true));

        return list;
    }
}