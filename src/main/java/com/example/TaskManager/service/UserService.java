package com.example.TaskManager.service;

import com.example.TaskManager.dao.impl.UserDAOImpl;
import com.example.TaskManager.model.Role;
import com.example.TaskManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDAOImpl userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDAO.getUserByLogin(username);

        if(user.getUsername() == null){
            throw new UsernameNotFoundException("User not authorized.");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());

        return (UserDetails) new User(user.getId() , user.getUsername(), user.getPassword(),
                Role.valueOf(authority.getAuthority()), true);
    }
}
