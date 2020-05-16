package com.example.task.manager.dao.mapper;

import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.enums.Components;
import com.example.task.manager.model.enums.Status;
import com.example.task.manager.model.enums.Type;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TicketMapper implements RowMapper<Ticket> {

    private UserDAO userDAO;

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
        ticket.setComponents(Stream.of((String[]) resultSet.getArray("components").getArray())
                .map(Components::valueOf)
                .collect(Collectors.toList()));
        ticket.setStoryId(resultSet.getLong("story_id"));
        ticket.setNumber(resultSet.getLong("number"));

        return ticket;
    }
}
