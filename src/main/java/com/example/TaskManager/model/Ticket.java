package com.example.TaskManager.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Ticket {

    private long id;
    private String label;
    private String description;
    private User creator;
    private List<User> executors;
    private Type type;
    private Status status;
    private List<Component> components;
    private LocalDateTime time;

    public Ticket(String label, String description, User creator,
                  List<User> executors, Type type, Status status,
                  List<Component> components) {
        this.label = label;
        this.description = description;
        this.creator = creator;
        this.executors = executors;
        this.type = type;
        this.status = status;
        this.components = components;
        this.time = LocalDateTime.now();
    }
}
