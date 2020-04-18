package com.example.TaskManager.DAOImpl;

import com.example.TaskManager.DAO.TicketDAO;
import com.example.TaskManager.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

public class TicketDAOImpl implements TicketDAO {

    JdbcTemplate jdbcTemplate;

    private final String SQL_GET_ALL = "select * from tickets";
    private final String SQL_FIND_TICKET_BY_TYPE = "select * from tickets where type = ?";
    private final String SQL_FIND_TICKET_BY_TIME = "select * from tickets where time = ?";
    private final String SQL_FIND_TICKET_BY_CREATOR = "select * from tickets where creator = ?";
    private final String SQL_DELETE_TICKET = "delete from tickets where id = ?";
    private final String SQL_UPDATE_TICKET = "update tickets set label = ?, description = ?, " +
            "creator  = ?, executors = ?, type = ?, status = ?, components = ?, time = ? where id = ?";
    private final String SQL_INSERT_TICKET = "insert into tickets " +
            "(label, description, creator, executors, type, status, components, time) " +
            "values(?,?,?,?,?,?,?,?)";

    @Autowired
    public TicketDAOImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Ticket> getAllTickets() {
        return jdbcTemplate.query(SQL_GET_ALL, new TicketMapper());
    }

    @Override
    public Ticket getTicketByType(Type type) {
        return jdbcTemplate.queryForObject(SQL_FIND_TICKET_BY_TYPE, new Object[] { type }, new TicketMapper());

    }

    @Override
    public Ticket getTicketByTime(LocalDateTime time) {
        return jdbcTemplate.queryForObject(SQL_FIND_TICKET_BY_TIME, new Object[] { time }, new TicketMapper());
    }

    @Override
    public Ticket getTicketByCreator(User user) {
        return jdbcTemplate.queryForObject(SQL_FIND_TICKET_BY_CREATOR, new Object[] { user }, new TicketMapper());
    }

    @Override
    public boolean deleteTicket(Ticket ticket) {
        return jdbcTemplate.update(SQL_DELETE_TICKET, ticket.getId()) > 0;
    }

    @Override
    public boolean updateTicket(Ticket ticket) {
        return jdbcTemplate.update(SQL_UPDATE_TICKET, ticket.getLabel(), ticket.getDescription(),
                ticket.getCreator(), ticket.getExecutors(), ticket.getType(),
                ticket.getStatus(), ticket.getComponents(), ticket.getTime(),
                ticket.getId()) > 0;
    }

    @Override
    public boolean addTicket(Ticket ticket) {
        return jdbcTemplate.update(SQL_INSERT_TICKET, ticket.getLabel(), ticket.getDescription(),
                ticket.getCreator(), ticket.getExecutors(), ticket.getType(),
                ticket.getStatus(), ticket.getComponents(), ticket.getTime()) > 0;
    }
}
