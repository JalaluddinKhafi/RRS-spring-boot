package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.Ticket;
import jakarta.transaction.Transactional;

import java.util.List;

public interface TicketService {

    @Transactional
    public void bookTicket(Ticket ticket);
    Ticket saveTicket(Ticket ticket);
    Ticket getTicketById(Long id);
    List<Ticket> getAllTickets();
    void deleteTicket(Long id);

    @Transactional
    public void cancelTicket(Long ticketId);
}
