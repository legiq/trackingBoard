package com.example.task.manager.dao.impl;

import com.example.task.manager.dao.TicketDAO;
import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.dao.mapper.TicketMapper;
import com.example.task.manager.dao.mapper.TicketQuery;
import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Component
public class TicketDAOImpl implements TicketDAO {

    private JdbcTemplate jdbcTemplate;
    private TicketMapper ticketMapper;

    private final String sqlGetAll;
    private final String sqlFindTicketById;
    private final String sqlFindTicketByCreator;
    private final String sqlDeleteAllTicketExecutors;
    private final String sqlDeleteTicket;
    private final String sqlUpdateTicket;
    private final String sqlInsertTicketExecutors;
    private final String sqlInsertTicket;
    private final String sqlAddExecutorToTicket;
    private final String sqlGetAllStoryTicket;
    private final String sqlDeleteExecutorFromTicket;
    private final String sqlFindTicketByNumber;

    public TicketDAOImpl(DataSource dataSource, UserDAO userDAO) throws IOException {

        jdbcTemplate = new JdbcTemplate(dataSource);

        this.ticketMapper = new TicketMapper(userDAO);

        TicketQuery ticketQuery = new ObjectMapper(new YAMLFactory())
                .findAndRegisterModules()
                .readValue(new File("src/main/resources/ticketQuery.yml"), TicketQuery.class);

        sqlGetAll = ticketQuery.getGetAllTickets();
        sqlFindTicketById = ticketQuery.getFindTicketById();
        sqlFindTicketByCreator = ticketQuery.getFindTicketByCreator();
        sqlDeleteAllTicketExecutors = ticketQuery.getDeleteTicketExecutors();
        sqlDeleteTicket = ticketQuery.getDeleteTicket();
        sqlUpdateTicket = ticketQuery.getUpdateTicket();
        sqlInsertTicketExecutors = ticketQuery.getInsertTicketExecutors();
        sqlInsertTicket = ticketQuery.getInsertTicket();
        sqlAddExecutorToTicket = ticketQuery.getAddExecutorToTicket();
        sqlGetAllStoryTicket = ticketQuery.getGetStoryTickets();
        sqlDeleteExecutorFromTicket = ticketQuery.getDeleteExecutorFromTicket();
        sqlFindTicketByNumber = ticketQuery.getFindTicketByNumber();
    }

    @Override
    public List<Ticket> getAllTickets() {
        return jdbcTemplate.query(sqlGetAll, ticketMapper);
    }

    @Override
    public Ticket getTicketById(Long id) {
        return jdbcTemplate.queryForObject(sqlFindTicketById, new Object[] { id }, ticketMapper);
    }

    @Override
    public List<Ticket> getTicketByCreator(Long userId) {
        return jdbcTemplate.query(sqlFindTicketByCreator, new Object[] { userId }, ticketMapper);
    }

    @Override
    public boolean deleteTicket(Ticket ticket) {

        for (Ticket subTicket : getAllStoryTickets(ticket)) {
            subTicket.setStoryId(0L);
            updateTicket(subTicket);
        }

        jdbcTemplate.update(sqlDeleteAllTicketExecutors, ticket.getId());

        return jdbcTemplate.update(sqlDeleteTicket, ticket.getId()) > 0;
    }

    @Override
    public boolean updateTicket(Ticket ticket) {

        String[] components = ticket.getComponents().stream()
                .map(Enum::toString)
                .toArray(String[]::new);

        return jdbcTemplate.update(sqlUpdateTicket, ticket.getLabel(), ticket.getDescription(),
                ticket.getCreator().getId(), ticket.getType().toString(),
                ticket.getStatus().toString(), components, ticket.getTime(),
                ticket.getStoryId() == 0 ? null : ticket.getStoryId(), ticket.getId()) > 0;
    }

    @Override
    public boolean addTicket(Ticket ticket) {

        String[] components = ticket.getComponents().stream()
                .map(Enum::toString)
                .toArray(String[]::new);

        int result = jdbcTemplate.update(sqlInsertTicket, ticket.getLabel(), ticket.getDescription(),
                ticket.getCreator().getId(), ticket.getType().toString(),
                ticket.getStatus().toString(), components, ticket.getTime(),
                ticket.getStoryId());

        Long ticketIndex = getAllTickets().stream()
                .map(Ticket::getId)
                .max(Comparator.comparingLong(a -> a))
                .orElse(0L);

        for(User user : ticket.getExecutors()) {
            jdbcTemplate.update(sqlInsertTicketExecutors, ticketIndex, user.getId());
        }

        return  result > 0;
    }

    @Override
    public boolean addExecutorToTicket(Ticket ticket) {

        List<User> executors = ticket.getExecutors();

        return jdbcTemplate.update(sqlAddExecutorToTicket, ticket.getId(),
                executors.get(executors.size()-1).getId()) > 0;
    }

    @Override
    public List<Ticket> getAllStoryTickets(Ticket story) {
        return jdbcTemplate.query(sqlGetAllStoryTicket, new Object[]{story.getId()}, ticketMapper);
    }

    @Override
    public boolean deleteExecutorFromTicket(Long ticketId, Long executorId) {
        return jdbcTemplate.update(sqlDeleteExecutorFromTicket, ticketId, executorId) > 0;
    }

    @Override
    public Ticket getTicketByNumber(Long ticketNumber) {
        return jdbcTemplate.queryForObject(sqlFindTicketByNumber, new Object[] { ticketNumber }, ticketMapper);
    }
}
