package model;

import java.util.Date;

public class Ticket {
    String ticketName;
    String description;
    String assignee;
    String reporter;
    Date spentTime;
    Date estimatedTime;

    public Ticket(String ticketName,
                  String description,
                  String assignee,
                  String reporter,
                  Date spentTime,
                  Date estimatedTime) {
        this.ticketName = ticketName;
        this.description = description;
        this.assignee = assignee;
        this.reporter = reporter;
        this.spentTime = spentTime;
        this.estimatedTime = estimatedTime;
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
                '}';
    }
}
