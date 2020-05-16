package com.example.task.manager.unit;

import com.example.task.manager.dao.TicketDAO;
import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Components;
import com.example.task.manager.model.enums.Role;
import com.example.task.manager.model.enums.Status;
import com.example.task.manager.model.enums.Type;
import com.example.task.manager.service.AuthService;
import com.example.task.manager.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private TicketDAO ticketDAO;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TicketService ticketService;

    @Test
    public void getAllTicketsTest() {

        Mockito.when(ticketDAO.getAllTickets()).thenReturn(createTestList());

        List<Ticket> actual = ticketService.getAllTickets();

        assertEquals("test task", actual.get(0).getLabel());
        assertEquals("info bug", actual.get(1).getDescription());
        Mockito.verify(ticketDAO, Mockito.times(1)).getAllTickets();
    }

    @Test
    public void getTicketByIdTest() {

        Mockito.when(ticketDAO.getTicketById(1L)).thenReturn(createTestList().get(0));

        Ticket actual = ticketService.getTicketById(1L);

        assertEquals("test task", actual.getLabel());
        assertEquals("info task", actual.getDescription());
        Mockito.verify(ticketDAO, Mockito.times(1)).getTicketById(1L);
    }

    @Test
    public void getAllStoryTicket() {

        Ticket story = createTestList().get(2);

        Mockito.when(ticketDAO.getAllStoryTickets(story)).thenReturn(Collections.singletonList(createTestList().get(1)));

        List<Ticket> actual = ticketService.getAllStoryTickets(story);

        assertEquals("test bug", actual.get(0).getLabel());
        assertEquals("info bug", actual.get(0).getDescription());
        assertEquals(3L, actual.get(0).getStoryId());
        Mockito.verify(ticketDAO, Mockito.times(1)).getAllStoryTickets(story);
    }

    @Test
    public void getTicketsByFilterTest() {

        Mockito.when(ticketDAO.getAllTickets()).thenReturn(createTestList());

        List<Ticket> actual1 = ticketService
                .getTicketsByFilter("Task", "creator", "");
        List<Ticket> actual2 = ticketService
                .getTicketsByFilter("asddsa", "asdasd", "asddsa");


        assertEquals(1, actual1.size());
        assertEquals(0, actual2.size());
        assertEquals(Type.Task, actual1.get(0).getType());
        Mockito.verify(ticketDAO, Mockito.times(2)).getAllTickets();
    }

    @Test
    public void addTicketTest() {

        User executor = new User(1L, "Oleg", "password", Role.Admin, true);
        User creator = new User(2L, "Adolf", "password", Role.Admin, true);

        Mockito.when(authService.isExists("Oleg")).thenReturn(true);
        Mockito.when(userDAO.getUserByLogin("Oleg")).thenReturn(executor);

        ticketService.addTicket(creator, "ticket", "info", "Oleg",
                "Task", "ToDo", "Backend", 0L);

        Ticket ticket = new Ticket("ticket", "info", creator, Collections.singletonList(executor),
                Type.Task, Status.ToDo, Collections.singletonList(Components.Backend));

        Mockito.verify(authService, Mockito.times(1)).isExists("Oleg");
        Mockito.verify(userDAO, Mockito.times(1)).getUserByLogin("Oleg");
        Mockito.verify(ticketDAO, Mockito.times(1)).addTicket(ticket);
    }

    @Test
    public void deleteTicketTest() {

        Ticket ticket = createTestList().get(0);

        Mockito.when(ticketDAO.getTicketById(1L)).thenReturn(ticket);

        ticketService.deleteTicket(1L);

        Mockito.verify(ticketDAO, Mockito.times(1)).getTicketById(1L);
        Mockito.verify(ticketDAO, Mockito.times(1)).deleteTicket(ticket);
    }

    @Test
    public void updateDescriptionAndGetTest() {

        Ticket ticket = createTestList().get(0);

        Mockito.when(ticketDAO.getTicketById(1L)).thenReturn(ticket);

        Ticket newTicket = ticketService.updateDescriptionAndGet(1L, "ultra new info");

        assertEquals("ultra new info", newTicket.getDescription());
        Mockito.verify(ticketDAO, Mockito.times(1)).getTicketById(1L);
        Mockito.verify(ticketDAO, Mockito.times(1)).updateTicket(newTicket);
    }

    @Test
    public void addExecutorToTicketTest() {

        Ticket ticket = createTestList().get(0);
        User user = new User(1L, "Oleg", "password", Role.Admin, true);

        Mockito.when(ticketDAO.getTicketById(1L)).thenReturn(ticket);
        Mockito.when(userDAO.getUserByLogin("Oleg")).thenReturn(user);

        ticketService.addExecutorToTicket(1L, "Oleg");

        Mockito.verify(ticketDAO, Mockito.times(1)).getTicketById(1L);
        Mockito.verify(userDAO, Mockito.times(1)).getUserByLogin("Oleg");
        Mockito.verify(ticketDAO, Mockito.times(1)).addExecutorToTicket(ticket);
    }

    @Test
    public void updateToNextStatusOrToDoStatusTest() {

        Ticket ticket = createTestList().get(0);

        Mockito.when(ticketDAO.getTicketById(1L)).thenReturn(ticket);

        ticketService.updateToNextStatus(1L);
        ticketService.updateToTodoStatus(1L);

        Mockito.verify(ticketDAO, Mockito.times(2)).updateTicket(ticket);
    }

    @Test
    public void updateStoryIdTest() {

        Ticket ticket = createTestList().get(0);

        Mockito.when(ticketDAO.getTicketByNumber(1L)).thenReturn(ticket);

        ticketService.updateStoryId(1L, 2L);

        Mockito.verify(ticketDAO, Mockito.times(1)).updateTicket(ticket);
        Mockito.verify(ticketDAO, Mockito.times(1)).getTicketByNumber(1L);
    }

    @Test
    public void deleteExecutorFromTicketTest() {

        ticketService.deleteExecutorFromTicket(1L,2L);

        Mockito.verify(ticketDAO, Mockito.times(1))
                .deleteExecutorFromTicket(1L, 2L);
    }

    @Test
    public void isTicketNumberAppropriateTest() {

        Mockito.when(ticketDAO.getAllTickets()).thenReturn(createTestList());

        boolean actual1 = ticketService.isTicketNumberAppropriate("tbj-123321");
        boolean actual2 = ticketService.isTicketNumberAppropriate("tbj-123456");
        boolean actual3 = ticketService.isTicketNumberAppropriate("sdasdas-2312312");

        assertTrue(actual1);
        assertFalse(actual2);
        assertFalse(actual3);
        Mockito.verify(ticketDAO, Mockito.times(3)).getAllTickets();
    }

    private List<Ticket> createTestList() {

        List<Ticket> list = new ArrayList<>();

        User creator = new User(1L, "creator", "password", Role.Developer, true);
        User executor = new User(2L, "executor", "password", Role.QA, true);

        List<User> executors = new ArrayList<>();
        executors.add(executor);

        Ticket ticket = new Ticket("test task", "info task", creator, executors,
                Type.Task, Status.ToDo, Collections.singletonList(Components.Backend));

        ticket.setNumber(123321L);

        list.add(ticket);

        Ticket subTicket = new Ticket("test bug", "info bug",
                creator, Collections.singletonList(executor), Type.Bug, Status.InProgress,
                Collections.singletonList(Components.Frontend));

        subTicket.setStoryId(3L);
        subTicket.setNumber(123L);

        Ticket story = new Ticket("test story", "info story",
                creator, Collections.singletonList(executor), Type.Story, Status.InTest,
                Collections.singletonList(Components.DataScience));

        story.setId(3L);
        story.setNumber(321L);

        list.add(subTicket);
        list.add(story);

        return list;
    }
}
