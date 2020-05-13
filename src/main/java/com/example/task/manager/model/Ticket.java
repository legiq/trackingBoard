package com.example.task.manager.model;

import com.example.task.manager.model.enums.Components;
import com.example.task.manager.model.enums.Status;
import com.example.task.manager.model.enums.Type;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class Ticket {

    private Long id;
    private String label;
    private String description;
    private User creator;
    private List<User> executors;
    private Type type;
    private Status status;
    private List<Components> components;
    private Date time;
    private Long storyId;

    public Ticket(String label, String description, User creator,
                  List<User> executors, Type type, Status status,
                  List<Components> components
    ) {
        this.label = label;
        this.description = description;
        this.creator = creator;
        this.executors = executors;
        this.type = type;
        this.status = status;
        this.components = components;
        this.time = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    public boolean isExecutor(User user) {
        return executors.contains(user);
    }
}
