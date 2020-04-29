package com.example.task.manager.model.enums;


public enum Status {

    ToDo, InProgress, InTest, Done;

    private Status nextStatus;

    static {
        ToDo.nextStatus = InProgress;
        InProgress.nextStatus = InTest;
        InTest.nextStatus = Done;
        Done.nextStatus = Done;
    }

    public Status getNextStatus() {
        return this.nextStatus;
    }
}
