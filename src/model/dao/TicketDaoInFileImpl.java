package model.dao;

import model.Priority;
import model.Status;
import model.Ticket;
import model.User;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TicketDaoInFileImpl implements TicketDao{
    private final List<Ticket> tickets = new ArrayList<>();
    private final String PATH = "src" + File.separator +
            "resources" + File.separator + "ticketDB.txt";
    private final String regexUserFields = ":";

    public TicketDaoInFileImpl() throws FileNotFoundException {
        loadTicketsFromFile(PATH);
    }

    @Override
    public void updateTicket(Ticket oldTicket, Ticket newTicket) throws IOException {
        updateTicketInFile(oldTicket, newTicket);
        tickets.set(tickets.indexOf(oldTicket), newTicket);
    }

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
        File file = new File(PATH);
        if (file.exists()) {
            try (
                    PrintWriter out = new PrintWriter(new FileWriter(file, true))
            ) {
                out.print(printTicketToLine(ticket));
            } catch (IOException e) {
                throw e;
            }
        } else {
            throw new IOException("File not found");
        }
    }

    private String printTicketToLine(Ticket ticket){
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return ticket.getTicketName() + regexUserFields +
                ticket.getDescription() + regexUserFields +
                ticket.getAssignee() + regexUserFields +
                ticket.getReporterName() + regexUserFields +
                ticket.getStatus() + regexUserFields +
                ticket.getPriority() + regexUserFields +
                format.format(ticket.getSpentTime()) + regexUserFields +
                format.format(ticket.getEstimatedTime()) + System.lineSeparator();
    }

    private void loadTicketsFromFile(String nameFile) throws FileNotFoundException {
        try (
                FileReader fileReader = new FileReader(nameFile);
                BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                addTicketFromLine(currentLine);
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTicketInFile(Ticket oldTicket, Ticket newTicket) throws IOException {
        Scanner sc;
        try {
            sc = new Scanner(new File(PATH));
        } catch (FileNotFoundException e) {
            throw e;
        }
        String fileContents;
        StringBuilder buffer = new StringBuilder();

        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()).append(System.lineSeparator());
        }
        fileContents = buffer.toString();
        sc.close();
        fileContents = fileContents.replaceAll(printTicketToLine(oldTicket), printTicketToLine(newTicket));
        FileWriter writer;
        try {
            writer = new FileWriter(PATH);
            writer.append(fileContents);
            writer.flush();
        } catch (IOException e) {
            throw e;
        }
    }

    private void addTicketFromLine(String line) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date dateSpent;
        Date dateEst;
        String[] array = line.split(regexUserFields);
        try {
            dateSpent = format.parse(array[6]);
            dateEst = format.parse(array[7]);
            tickets.add(new Ticket(array[0], array[1], array[2], array[3],
                    dateSpent, dateEst, Status.valueOf(array[4]), Priority.valueOf(array[5])));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
