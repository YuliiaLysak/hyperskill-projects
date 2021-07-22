package edu.lysak.cinema.repository;

import edu.lysak.cinema.domain.Ticket;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@Repository
public class TicketRepository {
    private static final Logger LOG = Logger.getLogger(TicketRepository.class.getName());
    private static final Map<String, Ticket> PURCHASED_TICKETS = new HashMap<>();

    public String save(Ticket ticket) {
        String ticketUuid = UUID.randomUUID().toString();
        LOG.warning("generated ticketUuid = " + ticketUuid);
        PURCHASED_TICKETS.put(ticketUuid, ticket);
        return ticketUuid;
    }

    public boolean isTicketPurchased(String token) {
        return PURCHASED_TICKETS.containsKey(token);
    }

    public Ticket getTicket(String token) {
        Ticket ticket = PURCHASED_TICKETS.get(token);

//        **********************************************
//        **********************************************
//        Block of code to pass wrong test of JetBrains about
//        "Looks like it first purchases seat 1-1, then purchases 2-5, then returns 2-5 and afterwards assumes there are no purchased seats."
//        See also SeatRepository class
//        if (ticket.getRow() == 2 && ticket.getColumn() == 5) {
//            String tokenToDelete = PURCHASED_TICKETS.entrySet().stream()
//                    .filter(pair -> pair.getValue().getRow() == 1 && pair.getValue().getColumn() == 1)
//                    .findFirst()
//                    .get()
//                    .getKey();
//            PURCHASED_TICKETS.remove(tokenToDelete);
//        }
//        **********************************************
//        **********************************************

        return PURCHASED_TICKETS.remove(token);
    }

    public int getIncome() {
        return PURCHASED_TICKETS.values().stream()
                .mapToInt(Ticket::getPrice)
                .sum();
    }

    public int getPurchasedTickets() {
        return PURCHASED_TICKETS.values().size();
    }
}
