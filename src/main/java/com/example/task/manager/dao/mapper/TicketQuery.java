package com.example.task.manager.dao.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketQuery {

    private String getAllTickets;
    private String findTicketById;
    private String findTicketByCreator;
    private String deleteTicketExecutors;
    private String deleteTicket;
    private String updateTicket;
    private String insertTicketExecutors;
    private String insertTicket;
    private String addExecutorToTicket;
    private String getStoryTickets;
    private String deleteExecutorFromTicket;
    private String findTicketByNumber;

}
