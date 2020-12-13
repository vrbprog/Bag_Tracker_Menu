package model.dao;

import model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketDaoInMemImpl implements TicketDao{
    private final List<Ticket> tickets = new ArrayList<>();

    public TicketDaoInMemImpl() {
    }

    @Override
    public void saveTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    @Override
    public List<Ticket> getAll() {
        return null;
    }
}
