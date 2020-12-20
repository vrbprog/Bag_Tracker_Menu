package service;

import model.Ticket;
import model.User;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    boolean createTicket(Ticket ticket);

    List<Ticket> getUserTickets(User user);

    Ticket getTicketByName(String nameTicket);

    boolean isTicket(Ticket ticket);

    boolean updateTicket(Ticket oldTicket, Ticket newTicket);

}
