package service;

import model.Ticket;
import model.User;

import java.util.Optional;

public interface TicketService {

    boolean createTicket(Ticket ticket);

    void showUserTicket(User user);

    Ticket getTicketByName(String nameTicket);
}
