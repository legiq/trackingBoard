package com.example.TaskManager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private long id;
    private String login;
    private String password;
    private Role role;

    public User (String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, login='%s', password='%s', role='%s']",
                id, login, password, role.toString());
    }
}
