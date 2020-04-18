package com.example.TaskManager.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketMapper implements RowMapper<Ticket> {

    @Override
    public Ticket mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Ticket ticket = new Ticket();
        ticket.setId(resultSet.getLong("id"));
        ticket.setLabel(resultSet.getString("label"));
        ticket.setDescription(resultSet.getString("description"));
        ticket.setCreator((User) resultSet.getObject("creator"));
        ticket.setExecutors((List<User>) resultSet.getObject("executors", ArrayList.class));
        ticket.setType(Type.valueOf(resultSet.getString("type")));
        ticket.setStatus(Status.valueOf(resultSet.getString("status")));
        ticket.setComponents((List<Component>) resultSet.getObject("components", ArrayList.class));
        ticket.setTime((LocalDateTime) resultSet.getObject("time"));

        return ticket;
    }
}
