package model;

import java.util.Date;
import java.util.Objects;

public class Ticket {

    private final String ticketName;
    private final String description;
    private final String assignee;
    private String reporter;
    private final Date spentTime;
    private Date estimatedTime;
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
        this.status = Status.TO_DO;
        this.priority = Priority.LOW;
    }
    public Ticket (Ticket copy){
        this.ticketName = copy.getTicketName();
        this.description = copy.getDescription();
        this.assignee = copy.getAssignee();
        this.reporter = copy.getReporterName();
        this.spentTime = copy.getSpentTime();
        this.estimatedTime = copy.getEstimatedTime();
        this.status = copy.getStatus();
        this.priority = copy.getPriority();
    }

    public Ticket(String ticketName,
                  String description,
                  String assignee,
                  String reporter,
                  Date spentTime,
                  Date estimatedTime,
                  Status status,
                  Priority priority) {
        this.ticketName = ticketName;
        this.description = description;
        this.assignee = assignee;
        this.reporter = reporter;
        this.spentTime = spentTime;
        this.estimatedTime = estimatedTime;
        this.status = status;
        this.priority = priority;
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

    public Status getStatus() {
        return status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public void setEstimatedTime(Date estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
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
                "\n\r  priority = " + priority + " " +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(ticketName, ticket.ticketName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketName);
    }
}
