package com.example.task.manager.integrational;

import com.example.task.manager.controller.UserController;
import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails(value = "admin")
@TestPropertySource("/application-test.properties")
@Sql(value={"/create-before-new.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private UserDAO userDAO;

    @WithUserDetails("user")
    @Test
    public void nonAdminUserTest() throws Exception {
        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void userListTest() throws Exception {
        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/div/div").nodeCount(2));
    }

    @Test
    public void userPageTest() throws Exception {
        this.mockMvc.perform(get("/user/{userId}", 1))
                .andDo(print())
                .andExpect(authenticated());
    }

    @Test
    public void addTicketTest() throws Exception {
        this.mockMvc.perform(post("/user/{userId}", 1)
                .param("userId", "1")
                .param("username", "newUser")
                .param("password", "123")
                .param("roleRadio", "Admin")
                .with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        assertEquals("newUser", userDAO.getUserById(1L).getUsername());
        assertEquals("123", userDAO.getUserById(1L).getPassword());
        assertEquals(Role.Admin, userDAO.getUserById(1L).getRole());
    }
}
