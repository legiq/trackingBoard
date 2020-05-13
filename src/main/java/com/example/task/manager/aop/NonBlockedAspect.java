package com.example.task.manager.aop;

import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NonBlockedAspect {

    private UserDAO userDAO;

    @Autowired
    public NonBlockedAspect(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Before(value="@annotation(NonBlockedCheck)")
    public void before(JoinPoint joinPoint) {

        User currentUser = userDAO.getUserById(
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

        if(!currentUser.isEnabled()) {
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        }
    }
}
