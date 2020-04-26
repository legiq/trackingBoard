package com.example.TaskManager.dao.mapper;

import com.example.TaskManager.dao.UserDAO;
import com.example.TaskManager.model.enums.Components;
import com.example.TaskManager.model.enums.Status;
import com.example.TaskManager.model.Ticket;
import com.example.TaskManager.model.enums.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TicketMapper implements RowMapper<Ticket> {

    private UserDAO userDAO;

    @Autowired
    public TicketMapper (UserDAO user) {
        this.userDAO = user;
    }

    @Override
    public Ticket mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        Ticket ticket = new Ticket();
        ticket.setId(resultSet.getLong("id"));
        ticket.setLabel(resultSet.getString("label"));
        ticket.setDescription(resultSet.getString("description"));
        ticket.setType(Type.valueOf(resultSet.getString("type")));
        ticket.setStatus(Status.valueOf(resultSet.getString("status")));
        ticket.setTime((Date) resultSet.getObject("time"));

        ticket.setCreator(userDAO.getUserById(resultSet.getLong("creator_id")));

        ticket.setExecutors(userDAO.getUsersFromTicket(resultSet.getLong("id")));

        ticket.setComponents(Stream.of(resultSet.getObject("components"))
                .map(String::valueOf)
                .map(s -> Stream.of(s.substring(1, s.length() - 1).split(","))
                        .map(Components::valueOf).collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList()));

        return ticket;
    }
}
