package service;

import model.Ticket;
import model.User;

public interface TicketService {

    boolean createTicket(Ticket ticket);

    void showUserTicket(User user);
}
