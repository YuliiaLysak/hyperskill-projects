package edu.lysak.cinema.domain.dto;

import edu.lysak.cinema.domain.Ticket;

public class ReturnedTicket {
    private Ticket returnedTicket;

    public ReturnedTicket() {
    }

    public ReturnedTicket(Ticket returnedTicket) {
        this.returnedTicket = returnedTicket;
    }

    public Ticket getReturnedTicket() {
        return returnedTicket;
    }

    public void setReturnedTicket(Ticket returnedTicket) {
        this.returnedTicket = returnedTicket;
    }
}
