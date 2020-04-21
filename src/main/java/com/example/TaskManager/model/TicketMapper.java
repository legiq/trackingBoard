package com.example.TaskManager.model;

import com.example.TaskManager.dao.impl.UserDAOImpl;
import com.example.TaskManager.dao.UserDAO;
import com.example.TaskManager.config.JdbcConfig;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
public class TicketMapper implements RowMapper<Ticket> {

    private UserDAO userDAO = new UserDAOImpl(new JdbcConfig().dataSource());

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

        ticket.setExecutors(userDAO.getUsersFromTicket(resultSet.getLong("executor_id")));

        ticket.setComponents(Stream.of(resultSet.getObject("components"))
                .map(String::valueOf)
                .map(s -> s.substring(1, s.length()-1))
                .map(Components::valueOf)
                .collect(Collectors.toList()));

        return ticket;
    }
}
