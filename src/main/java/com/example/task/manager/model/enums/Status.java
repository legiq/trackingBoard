package com.example.task.manager.model.enums;


public enum Status {

    ToDo, InProgress, InTest, Done;

    public Status getNextStatus() {
        return Status.values()[(this.ordinal()+1)];
    }
}
