package model.dao;

import model.Ticket;
import java.util.List;

public interface TicketDao {
    boolean saveTicket(Ticket ticket);

    List<Ticket> getAll();

}
