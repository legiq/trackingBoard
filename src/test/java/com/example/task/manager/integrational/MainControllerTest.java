package com.example.task.manager.integrational;

import com.example.task.manager.controller.MainController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("user")
@TestPropertySource("/application-test.properties")
@Sql(value={"/create-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value={"/create-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MainController mainController;

    @Test
    public void mainPageTest() throws Exception {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"navbarSupportedContent\"]/div[1]").string("user"));
    }

    @Test
    public void ticketCountTest() throws Exception {
        this.mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"ticket\"]").nodeCount(2));
    }

    @Test
    public void filterTest() throws Exception {
        this.mockMvc.perform(post("/filter").param("filterByType", "Task").with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"ticket\"]").nodeCount(1))
                .andExpect(xpath("//*[@id=\"ticket\"][@data-id=1]").exists());
    }

    @Test
    public void addTicketTest() throws Exception {
        this.mockMvc.perform(post("/main")
                .param("label", "test task")
                .param("description", "info")
                .param("executorLogin", "qaUser")
                .param("type", "Task")
                .param("status", "InTest")
                .param("components", "DataScience")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id=\"ticket\"]").nodeCount(3));
    }
}
