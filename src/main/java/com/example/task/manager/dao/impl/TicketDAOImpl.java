package com.example.task.manager.dao.impl;

import com.example.task.manager.dao.TicketDAO;
import com.example.task.manager.dao.UserDAO;
import com.example.task.manager.dao.mapper.TicketMapper;
import com.example.task.manager.dao.mapper.TicketQuery;
import com.example.task.manager.model.Ticket;
import com.example.task.manager.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
public class TicketDAOImpl implements TicketDAO {

    private JdbcTemplate jdbcTemplate;
    private TicketMapper ticketMapper;

    private final String SQL_GET_ALL;
    private final String SQL_FIND_TICKET_BY_ID;
    private final String SQL_FIND_TICKET_BY_TYPE;
    private final String SQL_FIND_TICKET_BY_TIME;
    private final String SQL_FIND_TICKET_BY_CREATOR;
    private final String SQL_DELETE_TICKET_EXECUTORS;
    private final String SQL_DELETE_TICKET;
    private final String SQL_UPDATE_TICKET;
    private final String SQL_INSERT_TICKET_EXECUTORS;
    private final String SQL_INSERT_TICKET;
    private final String SQL_ADD_EXECUTOR_TO_TICKET;
    private final String SQL_GET_ALL_STORY_TICKETS;
    private final String SQL_FIND_BY_ALL_FILTERS;
    private final String SQL_FIND_BY_CREATOR_AND_TIME;
    private final String SQL_FIND_BY_CREATOR_AND_TYPE;
    private final String SQL_FIND_BY_TIME_AND_TYPE;
    private final String SQL_DELETE_EXECUTOR_FROM_TICKET;

    @Autowired
    public TicketDAOImpl(DataSource dataSource, UserDAO userDAO) throws IOException {

        jdbcTemplate = new JdbcTemplate(dataSource);

        this.ticketMapper = new TicketMapper(userDAO);

        TicketQuery ticketQuery = new ObjectMapper(new YAMLFactory())
                .findAndRegisterModules()
                .readValue(new File("src/main/resources/ticketQuery.yml"), TicketQuery.class);

        SQL_GET_ALL = ticketQuery.getGetAllTickets();
        SQL_FIND_TICKET_BY_ID = ticketQuery.getFindTicketById();
        SQL_FIND_TICKET_BY_TYPE = ticketQuery.getFindTicketByType();
        SQL_FIND_TICKET_BY_TIME = ticketQuery.getFindTicketByTime();
        SQL_FIND_TICKET_BY_CREATOR = ticketQuery.getFindTicketByCreator();
        SQL_DELETE_TICKET_EXECUTORS = ticketQuery.getDeleteTicketExecutors();
        SQL_DELETE_TICKET = ticketQuery.getDeleteTicket();
        SQL_UPDATE_TICKET = ticketQuery.getUpdateTicket();
        SQL_INSERT_TICKET_EXECUTORS = ticketQuery.getInsertTicketExecutors();
        SQL_INSERT_TICKET = ticketQuery.getInsertTicket();
        SQL_ADD_EXECUTOR_TO_TICKET = ticketQuery.getAddExecutorToTicket();
        SQL_GET_ALL_STORY_TICKETS = ticketQuery.getGetStoryTickets();
        SQL_FIND_BY_ALL_FILTERS = ticketQuery.getFindTicketsByAllFilters();
        SQL_FIND_BY_CREATOR_AND_TIME = ticketQuery.getFindTicketsByCreatorAndTime();
        SQL_FIND_BY_CREATOR_AND_TYPE = ticketQuery.getFindTicketsByCreatorAndType();
        SQL_FIND_BY_TIME_AND_TYPE = ticketQuery.getFindTicketsByTimeAndType();
        SQL_DELETE_EXECUTOR_FROM_TICKET = ticketQuery.getDeleteExecutorFromTicket();
    }

    @Override
    public List<Ticket> getAllTickets() {
        return jdbcTemplate.query(SQL_GET_ALL, ticketMapper);
    }

    @Override
    public Ticket getTicketById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_TICKET_BY_ID, new Object[] { id }, ticketMapper);
    }

    @Override
    public List<Ticket> getTicketByType(String type) {
        return jdbcTemplate.query(SQL_FIND_TICKET_BY_TYPE, new Object[] { type }, ticketMapper);
    }

    @Override
    public List<Ticket> getTicketByTime(Date time) {
        return jdbcTemplate.query(SQL_FIND_TICKET_BY_TIME, new Object[] { time }, ticketMapper);
    }

    @Override
    public List<Ticket> getTicketByCreator(User user) {
        return jdbcTemplate.query(SQL_FIND_TICKET_BY_CREATOR, new Object[] { user.getId() }, ticketMapper);
    }

    @Override
    public boolean deleteTicket(Ticket ticket) {

        for (Ticket subTicket : getAllStoryTickets(ticket)) {
            subTicket.setStoryId(0L);
            updateTicket(subTicket);
        }

        jdbcTemplate.update(SQL_DELETE_TICKET_EXECUTORS, ticket.getId());

        return jdbcTemplate.update(SQL_DELETE_TICKET, ticket.getId()) > 0;
    }

    @Override
    public boolean updateTicket(Ticket ticket) {

        String[] components = ticket.getComponents().stream()
                .map(Enum::toString)
                .toArray(String[]::new);

        return jdbcTemplate.update(SQL_UPDATE_TICKET, ticket.getLabel(), ticket.getDescription(),
                ticket.getCreator().getId(), ticket.getType().toString(),
                ticket.getStatus().toString(), components, ticket.getTime(),
                ticket.getStoryId() == 0 ? null : ticket.getStoryId(), ticket.getId()) > 0;
    }

    @Override
    public boolean addTicket(Ticket ticket) {

        String[] components = ticket.getComponents().stream()
                .map(Enum::toString)
                .toArray(String[]::new);

        int result = jdbcTemplate.update(SQL_INSERT_TICKET, ticket.getLabel(), ticket.getDescription(),
                ticket.getCreator().getId(), ticket.getType().toString(),
                ticket.getStatus().toString(), components, ticket.getTime(),
                ticket.getStoryId());

        Long ticketIndex = getAllTickets().stream()
                .map(Ticket::getId)
                .max(Comparator.comparingLong(a -> a))
                .orElse(0L);

        for(User user : ticket.getExecutors()) {
            jdbcTemplate.update(SQL_INSERT_TICKET_EXECUTORS, ticketIndex, user.getId());
        }

        return  result > 0;
    }

    @Override
    public boolean addExecutorToTicket(Ticket ticket) {

        List<User> executors = ticket.getExecutors();

        return jdbcTemplate.update(SQL_ADD_EXECUTOR_TO_TICKET, ticket.getId(),
                executors.get(executors.size()-1).getId()) > 0;
    }

    @Override
    public List<Ticket> getAllStoryTickets(Ticket story) {
        return jdbcTemplate.query(SQL_GET_ALL_STORY_TICKETS, new Object[]{story.getId()}, ticketMapper);
    }

    @Override
    public List<Ticket> getByAllFilters(User creator, String type, Date time) {
        return jdbcTemplate.query(SQL_FIND_BY_ALL_FILTERS, new Object[]{creator.getId(), type, time}, ticketMapper);
    }

    @Override
    public List<Ticket> getByCreatorAndTime(User creator, Date time) {
        return jdbcTemplate.query(SQL_FIND_BY_CREATOR_AND_TIME, new Object[]{creator.getId(), time}, ticketMapper);
    }

    @Override
    public List<Ticket> getByCreatorAndType(User creator, String type) {
        return jdbcTemplate.query(SQL_FIND_BY_CREATOR_AND_TYPE, new Object[]{creator.getId(), type}, ticketMapper);
    }

    @Override
    public List<Ticket> getByTimeAndType(Date time, String type) {
        return jdbcTemplate.query(SQL_FIND_BY_TIME_AND_TYPE, new Object[]{time, type}, ticketMapper);
    }

    @Override
    public boolean deleteExecutorFromTicket(Long ticketId, Long executorId) {
        return jdbcTemplate.update(SQL_DELETE_EXECUTOR_FROM_TICKET, ticketId, executorId) > 0;
    }
}
