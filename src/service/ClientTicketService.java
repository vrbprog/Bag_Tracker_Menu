package service;

import model.Ticket;
import model.User;
import model.dao.TicketDao;
import model.dao.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientTicketService implements TicketService{

    private final TicketDao ticketDao;

    public ClientTicketService(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @Override
    public boolean createTicket(Ticket ticket) {
        return ticketDao.saveTicket(ticket);
    }

    @Override
    public void showUserTicket(User user) {
        List<Ticket> list;
        list = ticketDao.getAll().stream()
                .filter(ticket -> ticket.getReporterName()
                        .equals(user.getUserName())).collect(Collectors.toList());
        if(list.size()>0){
            for (Ticket ticket: list) {
                System.out.println(ticket);
            }
        }else{
            System.out.println("You have no tickets for which you are reportable");
        }

    }
}
