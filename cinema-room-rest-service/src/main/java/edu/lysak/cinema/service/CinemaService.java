package edu.lysak.cinema.service;

import edu.lysak.cinema.domain.Cinema;
import edu.lysak.cinema.domain.CinemaStatistic;
import edu.lysak.cinema.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class CinemaService {
    private final SeatService seatService;
    private final TicketRepository ticketRepository;

    public CinemaService(SeatService seatService, TicketRepository ticketRepository) {
        this.seatService = seatService;
        this.ticketRepository = ticketRepository;
    }

    public Cinema getInformation() {
        return new Cinema(9, 9, seatService.getSeats());
    }

    public CinemaStatistic getStatistics() {
        return new CinemaStatistic(
                ticketRepository.getIncome(),
                seatService.getAvailableSeats(),
                ticketRepository.getPurchasedTickets()
        );
    }
}
