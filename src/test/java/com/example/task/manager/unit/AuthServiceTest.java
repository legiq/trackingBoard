package com.example.task.manager.unit;

import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.User;
import com.example.task.manager.model.enums.Role;
import com.example.task.manager.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private AuthService authService;

    @Test
    public void isExistsTest() {

        List<User> users = new ArrayList<>();
        users.add(new User(1L, "Oleg", "password",Role.Admin,true));

        Mockito.when(userDAO.getAllUsers()).thenReturn(users);

        assertTrue(authService.isExists("Oleg"));
        assertFalse(authService.isExists("Adolf"));
        Mockito.verify(userDAO, Mockito.times(2)).getAllUsers();
    }
}
