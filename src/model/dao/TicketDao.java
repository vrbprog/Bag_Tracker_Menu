package model.dao;

import model.Ticket;
import java.util.List;

public interface TicketDao {
    void saveTicket(Ticket ticket);

    List<Ticket> getAll();
}
