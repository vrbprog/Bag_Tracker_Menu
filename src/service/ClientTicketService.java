package service;

import model.Ticket;
import model.User;
import model.dao.TicketDao;

import java.io.IOException;
import java.util.List;

import java.util.stream.Collectors;

public class ClientTicketService implements TicketService {

    private final TicketDao ticketDao;

    public ClientTicketService(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @Override
    public boolean createTicket(Ticket ticket) {
        try {
            ticketDao.saveTicket(ticket);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error access to file DB");
            return false;
        }
    }

    @Override
    public List<Ticket> getUserTickets(User user) {
        return ticketDao.getAll().stream()
                .filter(ticket -> ticket.getReporterName()
                        .equals(user.getUserName())).collect(Collectors.toList());
    }

    @Override
    public Ticket getTicketByName(String nameTicket) {
        return ticketDao.getAll().stream()
                .filter(ticket -> ticket.getTicketName()
                        .equals(nameTicket)).findFirst().orElse(null);

    }
}
