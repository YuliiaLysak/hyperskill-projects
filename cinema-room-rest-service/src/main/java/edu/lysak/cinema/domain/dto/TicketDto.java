package edu.lysak.cinema.domain.dto;

import edu.lysak.cinema.domain.Ticket;

public class TicketDto {
    private String token;
    private Ticket ticket;

    public TicketDto(String token, Ticket ticket) {
        this.token = token;
        this.ticket = ticket;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
}
