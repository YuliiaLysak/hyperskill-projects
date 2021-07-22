package edu.lysak.cinema.controller;

import edu.lysak.cinema.domain.Cinema;
import edu.lysak.cinema.domain.CinemaStatistic;
import edu.lysak.cinema.domain.dto.ReturnedTicket;
import edu.lysak.cinema.domain.dto.SeatDto;
import edu.lysak.cinema.domain.dto.TicketDto;
import edu.lysak.cinema.domain.dto.TokenDto;
import edu.lysak.cinema.service.CinemaService;
import edu.lysak.cinema.service.SeatService;
import edu.lysak.cinema.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@RestController
public class CinemaController {
    private static final Logger LOG = Logger.getLogger(CinemaController.class.getName());

    private final CinemaService cinemaService;
    private final SeatService seatService;
    private final TicketService ticketService;

    public CinemaController(CinemaService cinemaService, SeatService seatService, TicketService ticketService) {
        this.cinemaService = cinemaService;
        this.seatService = seatService;
        this.ticketService = ticketService;
    }

    @GetMapping("/seats")
    public Cinema getCinemaInformation() {
        LOG.warning("/seats");
        return cinemaService.getInformation();
    }

    @PostMapping("/purchase")
    public ResponseEntity<Object> purchaseTicket(@RequestBody SeatDto seatDto) {
        LOG.warning("/purchase");
        int row = seatDto.getRow();
        int column = seatDto.getColumn();
        if (!seatService.isSeatExist(row, column)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error",
                            "The number of a row or a column is out of bounds!"
                    ));
        }

        if (seatService.isSeatBooked(row, column)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error",
                            "The ticket has been already purchased!"
                    ));
        }

        TicketDto ticket = seatService.bookSeat(row, column);
        LOG.warning("purchased ticket = " + ticket.getToken());
        return ResponseEntity.ok(ticket);
    }

    @PostMapping("/return")
    public ResponseEntity<Object> returnTicket(@RequestBody TokenDto tokenDto) {
        LOG.warning("/return");
        LOG.warning("returning ticket = " + tokenDto.getToken());
        if (!ticketService.isTicketPurchased(tokenDto.getToken())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "error",
                            "Wrong token!"
                    ));
        }
        ReturnedTicket returnedTicket = ticketService.returnTicket(tokenDto.getToken());
//        LOG.warning("Ticket for returning -> " +
//                "row = " + returnedTicket.getReturnedTicket().getRow() +
//                " seat = " + returnedTicket.getReturnedTicket().getColumn());
        return ResponseEntity.ok(returnedTicket);
    }

    @PostMapping("/stats")
    public ResponseEntity<Object> getStatistics(@RequestParam(required = false) String password) {
        LOG.warning("/stats");
        if (password == null || !"super_secret".equals(password)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error",
                            "The password is wrong!"
                    ));
        }

        CinemaStatistic statistics = cinemaService.getStatistics();
        LOG.warning("current income = " + statistics.getCurrentIncome());
        return ResponseEntity.ok(statistics);
    }
}
