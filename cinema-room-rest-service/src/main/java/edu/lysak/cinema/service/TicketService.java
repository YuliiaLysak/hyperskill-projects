package edu.lysak.cinema.service;

import edu.lysak.cinema.domain.Seat;
import edu.lysak.cinema.domain.Ticket;
import edu.lysak.cinema.domain.dto.ReturnedTicket;
import edu.lysak.cinema.domain.dto.TicketDto;
import edu.lysak.cinema.repository.SeatRepository;
import edu.lysak.cinema.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    public TicketService(TicketRepository ticketRepository, SeatRepository seatRepository) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
    }

    public TicketDto purchaseTicket(Seat seat) {
        Ticket ticket = new Ticket(seat.getRow(), seat.getColumn(), seat.getPrice());
        String ticketUUID = ticketRepository.save(ticket);
        return new TicketDto(ticketUUID, ticket);
    }

    public boolean isTicketPurchased(String token) {
        return ticketRepository.isTicketPurchased(token);
    }

    public ReturnedTicket returnTicket(String token) {
        Ticket ticket = ticketRepository.getTicket(token);
        seatRepository.unbookSeat(ticket.getRow(), ticket.getColumn());
        return new ReturnedTicket(ticket);
    }
}
