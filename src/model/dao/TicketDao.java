package model.dao;

import model.Ticket;

import java.io.IOException;
import java.util.List;

public interface TicketDao {

    void saveTicket(Ticket ticket) throws IOException;

    List<Ticket> getAll();

}
