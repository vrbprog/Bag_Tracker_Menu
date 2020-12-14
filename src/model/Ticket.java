package model;

import java.util.Date;

public class Ticket {

    private final String ticketName;
    private final String description;
    private final String assignee;
    private final String reporter;
    private final Date spentTime;
    private final Date estimatedTime;
    private Status status;
    private Priority priority;

    public Ticket(String ticketName,
                  String description,
                  String assignee,
                  String reporter,
                  Date estimatedTime) {
        this.ticketName = ticketName;
        this.description = description;
        this.assignee = assignee;
        this.reporter = reporter;
        this.spentTime = new Date();
        this.estimatedTime = estimatedTime;
        this.status = Status.TODO;
        this.priority = Priority.LOW;
    }

    public String getTicketName() {
        return ticketName;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getReporterName() {
        return reporter;
    }

    public Date getSpentTime() {
        return spentTime;
    }

    public Date getEstimatedTime() {
        return estimatedTime;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "\n\r  ticketName = '" + ticketName + '\'' +
                "\n\r  description = '" + description + '\'' +
                "\n\r  assignee = '" + assignee + '\'' +
                "\n\r  reporter = '" + reporter + '\'' +
                "\n\r  spentTime = " + spentTime +
                "\n\r  estimatedTime = " + estimatedTime +
                "\n\r  status = " + status + " " +
                "\n\r  status = " + priority + " " +
                '}';
    }
}
