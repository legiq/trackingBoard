package com.example.TaskManager.model;

import com.example.TaskManager.model.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
public class User implements UserDetails {

    private long id;
    private String username;
    private String password;
    private Role role;
    private boolean active;

    public User (Long id, String login, String password, Role role, boolean active) {
        this.id = id;
        this.username = login;
        this.password = password;
        this.role = role;
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, login='%s', password='%s', role='%s', active='%s']",
                id, username, password, role.toString(), active);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(role);
        return roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
