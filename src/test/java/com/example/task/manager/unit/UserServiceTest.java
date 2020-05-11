package com.example.task.manager.unit;

import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Role;
import com.example.task.manager.service.AuthService;
import com.example.task.manager.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private AuthService authService;

    @BeforeTestMethod
    public void initMocks(){
        MockitoAnnotations.initMocks(this);
    }

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

    private List<User> createTestList() {
        List<User> list = new ArrayList<>();
        list.add(new User(1L, "oleg", "password", Role.Developer, true));
        list.add(new User(2L, "neoleg", "drowssap", Role.QA, true));

        return list;
    }
}