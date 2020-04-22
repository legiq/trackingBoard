package com.example.TaskManager.dao.impl;

import com.example.TaskManager.dao.TicketDAO;
import com.example.TaskManager.model.Ticket;
import com.example.TaskManager.model.TicketMapper;
import com.example.TaskManager.model.Type;
import com.example.TaskManager.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class TicketDAOImpl implements TicketDAO {


    private final String SQL_GET_ALL = "select * from tickets";
    private final String SQL_FIND_TICKET_BY_ID = "select * from tickets inner join users on " +
            " tickets.executor_id = users.id where tickets.id = ?";
    private final String SQL_FIND_TICKET_BY_TYPE = "select * from tickets where type = ?::ticket_type";
    private final String SQL_FIND_TICKET_BY_TIME = "select * from tickets where time = ?";
    private final String SQL_FIND_TICKET_BY_CREATOR = "select * from tickets where creator_id = ?";
    private final String SQL_DELETE_TICKET = "delete from tickets where id = ?";
    private final String SQL_UPDATE_TICKET = "update tickets set label = ?, description = ?, " +
            "creator_id  = ?, executor_id = ?, type = ?::ticket_type, status = ?::ticket_status," +
            " components = ?::ticket_component[], time = ? where id = ?";
    private final String SQL_INSERT_TICKET = "insert into tickets " +
            "(label, description, creator_id, executor_id, type, status, components, time) " +
            "values(?,?,?,?,?::ticket_type,?::ticket_status,?::ticket_component[],?)";


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public TicketDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private TicketMapper ticketMapper = new TicketMapper();

    @Override
    public List<Ticket> getAllTickets() {
        return jdbcTemplate.query(SQL_GET_ALL, ticketMapper);
    }

    @Override
    public Ticket getTicketById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_TICKET_BY_ID, new Object[] { id }, ticketMapper);
    }

    @Override
    public Ticket getTicketByType(Type type) {
        return jdbcTemplate.queryForObject(SQL_FIND_TICKET_BY_TYPE, new Object[] { type.toString() }, ticketMapper);
    }

    @Override
    public Ticket getTicketByTime(Date time) {
        return jdbcTemplate.queryForObject(SQL_FIND_TICKET_BY_TIME, new Object[] { time }, ticketMapper);
    }

    @Override
    public Ticket getTicketByCreator(User user) {
        return jdbcTemplate.queryForObject(SQL_FIND_TICKET_BY_CREATOR, new Object[] { user }, ticketMapper);
    }

    @Override
    public boolean deleteTicket(Ticket ticket) {
        return jdbcTemplate.update(SQL_DELETE_TICKET, ticket.getId()) > 0;
    }

    @Override
    public boolean updateTicket(Ticket ticket) {

        String[] components = ticket.getComponents().stream()
                .map(Enum::toString)
                .toArray(String[]::new);

        Long[] executors = ticket.getExecutors().stream()
                .map(User::getId)
                .toArray(Long[]::new);

        return jdbcTemplate.update(SQL_UPDATE_TICKET, ticket.getLabel(), ticket.getDescription(),
                ticket.getCreator().getId(), executors[0], ticket.getType().toString(),
                ticket.getStatus().toString(), components, ticket.getTime(),
                ticket.getId()) > 0;
    }

    @Override
    public boolean addTicket(Ticket ticket) {

        String[] components = ticket.getComponents().stream()
                .map(Enum::toString)
                .toArray(String[]::new);

        return jdbcTemplate.update(SQL_INSERT_TICKET, ticket.getLabel(), ticket.getDescription(),
                ticket.getCreator().getId(), ticket.getExecutors().get(0).getId(), ticket.getType().toString(),
                ticket.getStatus().toString(), components, ticket.getTime()) > 0;
    }
}
