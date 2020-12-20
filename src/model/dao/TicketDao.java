package model.dao;

import model.Ticket;
import model.User;

import java.io.IOException;
import java.util.List;

public interface TicketDao {

    void saveTicket(Ticket ticket) throws IOException;

    List<Ticket> getAll();

    void updateTicket(Ticket oldTicket, Ticket newTicket) throws IOException;

}
