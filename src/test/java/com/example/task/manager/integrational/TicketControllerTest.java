package com.example.task.manager.integrational;

import com.example.task.manager.controller.TicketController;
import com.example.task.manager.dao.TicketDAO;
import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails
@TestPropertySource("/application-test.properties")
@Sql(value={"/create-before-new.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketController ticketController;

    @Autowired
    private TicketDAO ticketDAO;

    @Test
    public void ticketPageTest() throws Exception {
        this.mockMvc.perform(get("/ticket/{ticketId}", 1))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div[1]/div[1]").string("tbj-100"))
                .andExpect(xpath("/html/body/div/div[1]/div[2]").string("Label: task"))
                .andExpect(xpath("/html/body/div/div[1]/div[4]").string("Creator: user"))
                .andExpect(xpath("/html/body/div/div[1]/div[6]").string("Status: ToDo"));
    }

    @Test
    public void updateDescriptionTest() throws Exception {
        this.mockMvc.perform(post("/ticket/{ticketId}", 2)
                .param("ticketId", "2")
                .param("description", "new description")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div[1]/div[1]").string("tbj-101"))
                .andExpect(xpath("/html/body/div/div[1]/div[2]").string("Label: bug"))
                .andExpect(xpath("/html/body/div/div[1]/div[4]").string("Creator: user"));

        assertEquals("new description", ticketDAO.getTicketById(2L).getDescription());
    }

    @Test
    public void addExecutorTest() throws Exception {
        this.mockMvc.perform(post("/ticket/add")
                .param("username", "qaUser")
                .param("ticketId", "1")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        assertTrue(ticketDAO.getTicketById(1L).getExecutors().stream()
                .map(User::getUsername)
                .collect(Collectors.toList()).contains("qaUser"));
    }

    @Test
    public void createBugTest() throws Exception {
        this.mockMvc.perform(post("/ticket/bug")
                .param("label", "added bug")
                .param("description", "info")
                .param("executorLogin", "qaUser")
                .param("type", "Bug")
                .param("status", "ToDo")
                .param("components", "DataScience")
                .param("ticketId", "3")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        assertEquals(4, ticketDAO.getAllTickets().size());
        assertEquals("added bug", ticketDAO.getTicketById(10L).getLabel());
    }

    @Test
    public void forwardTest() throws Exception {

        assertEquals(Status.ToDo, ticketDAO.getTicketById(1L).getStatus());

        this.mockMvc.perform(post("/ticket/forward")
                .param("ticketId", "1")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        assertEquals(Status.InProgress, ticketDAO.getTicketById(1L).getStatus());
    }

    @Test
    public void reopenTest() throws Exception {

        assertEquals(Status.InTest, ticketDAO.getTicketById(3L).getStatus());

        this.mockMvc.perform(post("/ticket/reopen")
                .param("ticketId", "3")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        assertEquals(Status.ToDo, ticketDAO.getTicketById(3L).getStatus());
    }

    @Test
    public void addSubTicketTest() throws Exception {

        assertEquals(Status.InTest, ticketDAO.getTicketById(3L).getStatus());

        this.mockMvc.perform(post("/ticket/addSubTicket")
                .param("ticketId", "3")
                .param("subTicketNumber", "tbj-100")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        assertEquals(Status.ToDo, ticketDAO.getTicketByNumber(100L).getStatus());
    }

    @Test
    public void deleteSubTicketTest() throws Exception {

        assertEquals(3L, ticketDAO.getTicketById(2L).getStoryId());

        this.mockMvc.perform(post("/ticket/deleteSubTicket")
                .param("ticketId", "3")
                .param("subTicketNumber", "101")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        assertEquals(0L, ticketDAO.getTicketById(2L).getStoryId());
    }

    @Test
    public void deleteExecutorTest() throws Exception {

        assertEquals(1, ticketDAO.getTicketById(1L).getExecutors().size());

        this.mockMvc.perform(post("/ticket/deleteExecutor")
                .param("ticketId", "1")
                .param("executorId", "1")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        assertEquals(0, ticketDAO.getTicketById(1L).getExecutors().size());
    }

    @Test
    public void createSubTicketTest() throws Exception {

        assertEquals(3, ticketDAO.getAllTickets().size());

        this.mockMvc.perform(post("/ticket/createSubTicket")
                .param("label", "added ticket")
                .param("description", "info")
                .param("executorLogin", "qaUser")
                .param("type", "Task")
                .param("status", "ToDo")
                .param("components", "DataScience")
                .param("ticketId", "3")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        assertEquals(4, ticketDAO.getAllTickets().size());
        assertEquals(3, ticketDAO.getTicketById(10L).getStoryId());
    }
}
