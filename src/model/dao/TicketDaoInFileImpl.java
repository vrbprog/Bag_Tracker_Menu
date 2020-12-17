package model.dao;

import model.Ticket;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TicketDaoInFileImpl implements TicketDao{
    private final List<Ticket> tickets = new ArrayList<>();
    private final String PATH = "src" + File.separator +
            "resources" + File.separator + "ticketDB.txt";
    private final String regexUserFields = ":";

    @Override
    public void saveTicket(Ticket ticket) throws IOException {
        tickets.add(ticket);
        writeTicketToFile(ticket);
    }

    @Override
    public List<Ticket> getAll() {
        return tickets;
    }

    private void writeTicketToFile(Ticket ticket) throws IOException {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        File file = new File(PATH);
        if (file.exists()) {
            try (
                    PrintWriter out = new PrintWriter(new FileWriter(file, true))
            ) {
                out.println(ticket.getTicketName() + regexUserFields +
                        ticket.getDescription() + regexUserFields +
                        ticket.getAssignee() + regexUserFields +
                        ticket.getReporterName() + regexUserFields +
                        ticket.getStatus() + regexUserFields +
                        ticket.getPriority() + regexUserFields +
                        format.format(ticket.getSpentTime()) + regexUserFields +
                        format.format(ticket.getEstimatedTime()));
            } catch (IOException e) {
                throw e;
            }
        } else {
            throw new IOException("File not found");
        }
    }
}
