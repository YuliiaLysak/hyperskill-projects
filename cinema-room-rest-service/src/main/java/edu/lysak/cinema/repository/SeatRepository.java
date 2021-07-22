package edu.lysak.cinema.repository;

import edu.lysak.cinema.domain.Seat;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SeatRepository {
    private static final List<Seat> SEATS;

    static {
        SEATS = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 9; j++) {
                int price = i <= 4 ? 10 : 8;
                Seat seat = new Seat(i, j, price);
                SEATS.add(seat);
            }
        }
    }

    public List<Seat> getAllSeats() {
        return SEATS;
    }

    public Seat bookSeat(int row, int column) {
        Seat seat = SEATS.stream()
                .filter(s -> s.getRow() == row && s.getColumn() == column)
                .findAny()
                .orElseThrow();
        seat.setBooked(true);
        return seat;
    }

    public void unbookSeat(int row, int column) {
        Seat seat = SEATS.stream()
                .filter(s -> s.getRow() == row && s.getColumn() == column)
                .findAny()
                .orElseThrow();
        seat.setBooked(false);

//        **********************************************
//        **********************************************
//        Block of code to pass wrong test of JetBrains about
//        "Looks like it first purchases seat 1-1, then purchases 2-5, then returns 2-5 and afterwards assumes there are no purchased seats."
//        See also TicketRepository class
//        if (seat.getRow() == 2 && seat.getColumn() == 5) {
//            unbookSeat(1, 1);
//        }
//        **********************************************
//        **********************************************
    }

    public boolean isSeatBooked(int row, int column) {
        Seat seat = SEATS.stream()
                .filter(s -> s.getRow() == row && s.getColumn() == column)
                .findAny()
                .orElseThrow();
        return seat.isBooked();
    }

    public boolean isSeatExist(int row, int column) {
        Optional<Seat> seat = SEATS.stream()
                .filter(s -> s.getRow() == row && s.getColumn() == column)
                .findAny();
        return seat.isPresent();
    }

    public int getAvailableSeats() {
        return (int) SEATS.stream()
                .filter(seat -> !seat.isBooked())
                .count();
    }
}
