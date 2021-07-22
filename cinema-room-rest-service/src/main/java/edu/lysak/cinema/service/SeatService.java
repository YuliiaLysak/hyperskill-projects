package edu.lysak.cinema.service;

import edu.lysak.cinema.domain.Seat;
import edu.lysak.cinema.domain.dto.TicketDto;
import edu.lysak.cinema.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final TicketService ticketService;

    public SeatService(SeatRepository seatRepository, TicketService ticketService) {
        this.seatRepository = seatRepository;
        this.ticketService = ticketService;
    }

    public TicketDto bookSeat(int row, int column) {
        Seat seat = seatRepository.bookSeat(row, column);
        return ticketService.purchaseTicket(seat);
    }

    public boolean isSeatBooked(int row, int column) {
        return seatRepository.isSeatBooked(row, column);
    }

    public boolean isSeatExist(int row, int column) {
        return seatRepository.isSeatExist(row, column);
    }

    public List<Seat> getSeats() {
        return seatRepository.getAllSeats();
    }

    public int getAvailableSeats() {
        return seatRepository.getAvailableSeats();
    }
}
