package com.example.task.manager.unit;

import com.example.task.manager.dao.TicketDAO;
import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Components;
import com.example.task.manager.model.enums.Role;
import com.example.task.manager.model.enums.Status;
import com.example.task.manager.model.enums.Type;
import com.example.task.manager.service.TicketService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TicketServiceTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private TicketDAO ticketDAO;

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

    private List<Ticket> createTestList() {

        List<Ticket> list = new ArrayList<>();

        User creator = new User(1L, "creator", "password", Role.Developer, true);
        User executor = new User(2L, "executor", "password", Role.QA, true);

        list.add(new Ticket("test task", "info task", creator, Collections.singletonList(executor),
                Type.Task, Status.ToDo, Collections.singletonList(Components.Backend)));

        Ticket subTicket = new Ticket("test bug", "info bug",
                creator, Collections.singletonList(executor), Type.Bug, Status.InProgress,
                Collections.singletonList(Components.Frontend));

        subTicket.setStoryId(3L);

        Ticket story = new Ticket("test story", "info story",
                creator, Collections.singletonList(executor), Type.Story, Status.InTest,
                Collections.singletonList(Components.DataScience));

        story.setId(3L);

        list.add(subTicket);
        list.add(story);

        return list;
    }
}
