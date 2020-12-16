package controller;

import model.Priority;
import model.Status;
import model.Ticket;
import model.User;
import model.dao.TicketDao;
import model.dao.TicketDaoInMemImpl;
import model.dao.UserDaoInFileImpl;
import service.ClientTicketService;
import service.ClientUserService;
import service.TicketService;
import service.UserService;
import viewConsole.*;
import model.dao.UserDao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    private final BaseMenu loginMenu;
    private final BaseMenu userTopMenu;
    private final BaseMenu userEditSubMenu;
    private final BaseMenu userEditStatusSubMenu;
    private final BaseMenu userEditPrioritySubMenu;
    private final Scanner scanner;
    private final UserService clientUserService;
    private final TicketService clientTicketService;
    private User currentUser;

    public Controller() throws FileNotFoundException {
        UserDao userDao = new UserDaoInFileImpl();
        //userDao = new UserDaoInMemImpl();
        TicketDao ticketDao = new TicketDaoInMemImpl();
        clientUserService = new ClientUserService(userDao);
        clientTicketService = new ClientTicketService(ticketDao);
        loginMenu = createMenu(LoginMenuItem.values(),
                "Login menu", "0. Exit from program");
        userTopMenu = createMenu(UserTopMenuItem.values(),
                "User top menu", "0. Exit from program");
        userEditSubMenu = createMenu(UserEditSubMenuItem.values(),
                "User edit menu", "0. Exit from edit menu");
        userEditStatusSubMenu = createMenu(Status.values(),
                "Edit status menu", "0. Exit from edit menu");
        userEditPrioritySubMenu = createMenu(Priority.values(),
                "Edit priority menu", "0. Exit from edit menu");
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            while (getChoiceUserLoginMenu()) {
                printInvalidMessage();
                //System.out.println("********************");
            }
            while (getChoiceUserTopMenu()) {
                System.out.println("********************");
            }
        }
    }

    private boolean getChoiceUserLoginMenu() {
        final int LOGIN = 1, REGISTRATION = 2, EXIT = 0;
        switch (loginMenu.show()) {
            case LOGIN: {
                System.out.println("Login ....");
                return loginSubMenu(scanner);
            }
            case REGISTRATION: {
                System.out.println("Registration .... ");
                return registerSubMenu(scanner);
            }
            case EXIT: {
                systemOut();
            }
            default: {
                return true;
            }
        }
    }

    private boolean getChoiceUserTopMenu() {
        final int CREATE_TICKET = 1, EDIT_TICKET = 2, MY_TICKET_LIST = 3,
                MY_DASHBOARD = 4, RETURN_IN_LOGIN_MENU = 5, EXIT = 0;
        switch (userTopMenu.show()) {
            case CREATE_TICKET: {
                System.out.println("Creating ticket ....");
                return createTicketSubMenu(scanner);
            }
            case EDIT_TICKET: {
                System.out.println("Editing ticket .... ");
                return editTicket(scanner);
            }
            case MY_TICKET_LIST: {
                System.out.println("My tickets list ....");
                showUserTickets();
                return true;
            }
            case MY_DASHBOARD: {
                showDashboard();
                return true;
            }
            case RETURN_IN_LOGIN_MENU: {
                return false;
            }
            case EXIT: {
                systemOut();
            }
            default: {
                return true;
            }
        }
    }

    private <T extends Enum<T>> BaseMenu createMenu(T[] aValues, String header, String bottom) {
        List<MenuItem> listLoginMenuItem = new ArrayList<>();
        listLoginMenuItem.add(new HeaderMenuItem(header));
        listLoginMenuItem.addAll(Arrays
                .stream(aValues).map(loginItem ->
                        new MenuItem(loginItem.toString()))
                .collect(Collectors.toList()));
        listLoginMenuItem.add(new BottomMenuItem(bottom));
        return new BaseMenu(listLoginMenuItem);
    }

    private boolean loginSubMenu(Scanner scanner) {
        System.out.print("Input login: ");
        String login = scanner.nextLine();

        System.out.print("Input password: ");
        String password = scanner.nextLine();

        if (clientUserService.login(login, password)) {
            currentUser = new User(login, password);
            printMessageMenu("Hello " + login + ". You are login in system");
            return false;
        } else {
            printMessageMenu("Wrong username/password");
            return true;
        }
    }

    private boolean registerSubMenu(Scanner scanner) {
        String login;
        String password;
        String passwordConfirm;

        System.out.print("Enter login: ");
        login = scanner.nextLine();

        if (clientUserService.loginIsBusy(login)) {
            printMessageMenu("This login is busy");
            return true;
        }
        System.out.print("Enter password: ");
        password = scanner.nextLine();

        System.out.print("Confirm password: ");
        passwordConfirm = scanner.nextLine();

        if (!password.equals(passwordConfirm)) {
            printMessageMenu("Confirm of password failed");
            return true;
        }

        currentUser = new User(login, password);
        try {
            if (clientUserService.userRegistration(currentUser)) {
                printMessageMenu("Hello " + login + ". You are registered in system");
                return false;
            } else {
                printMessageMenu("Registration failed!");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            printMessageMenu("Error access to file DB");
            return true;
        }
    }

    private boolean createTicketSubMenu(Scanner scanner) {
        System.out.print("Input name of ticket: ");
        String nameTicket = scanner.nextLine();

        System.out.print("Input description of ticket: ");
        String description = scanner.nextLine();

        String reporterUser = getReporterUser();

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date estDate = null;
        while (estDate == null) {
            System.out.println("Enter estimated deadline date in the format yyyy-MM-dd");
            System.out.println("For example, it is now " + format.format(new Date()));
            String line = scanner.nextLine();
            try {
                estDate = format.parse(line);
            } catch (ParseException e) {
                System.out.print("Sorry, that's not valid. Do you want to try again. (y/n): ");
                if (scanner.nextLine().equals("n")) return true;
            }
        }
        Ticket newTicket = new Ticket(nameTicket, description,
                currentUser.getUserName(),
                reporterUser, estDate);
        if (clientTicketService.createTicket(newTicket)) {
            printMessageMenu(currentUser.getUserName() + " create ticket:");
            System.out.println(newTicket);
            printMessageMenu("For return in menu press Enter");
            scanner.nextLine();
        } else {
            printMessageMenu("Writing ticket failed!");
        }
        return true;
    }

    private void showUserTickets() {
        clientTicketService.showUserTicket(currentUser);
        printMessageMenu("For return in menu press Enter");
        scanner.nextLine();
    }

    private boolean editTicket(Scanner scanner) {
        Ticket editTicket = null;
        boolean nameTickerNoPresent = true;
        while (nameTickerNoPresent) {
            System.out.print("Input name of editing ticket: ");
            String ticketName = scanner.nextLine();
            editTicket = clientTicketService.getTicketByName(ticketName);
            if (editTicket != null) {
                if (currentUser.getUserName().equals(editTicket.getAssignee())) {
                    nameTickerNoPresent = false;
                    System.out.print("Editing ");
                    System.out.println(editTicket);
                } else {
                    System.out.println("Sorry, you are not the one who assigned this ticket ");
                    return true;
                }
            } else {
                System.out.print("Sorry, that's not valid name of ticket. Do you want to try again. (y/n): ");
                if (scanner.nextLine().equals("n")) return true;
            }
        }

        printMessageMenu("You can edit fields: reporter, status, priority and estimated time");
        boolean isEditingTicket = true;
        while (isEditingTicket) {
            switch (userEditSubMenu.show()) {
                case 1: {
                    System.out.println("Editing reporter ....");
                    editReporterSubMenu(editTicket);
                    break;
                }
                case 2: {
                    System.out.println("Editing status .... ");
                    showEditStatusMenu(editTicket);
                    break;
                }
                case 3: {
                    System.out.println("Editing priority ....");
                    showEditPriorityMenu(editTicket);
                    break;
                }
                case 4: {
                    System.out.println("Editing estimated time ....");
                    showEditEstimatedTime(editTicket);
                    break;
                }
                case 0: {
                    isEditingTicket = false;
                    break;
                }
                default: {
                    printInvalidMessage();
                }
            }
        }
        return true;
    }

    private void showEditStatusMenu(Ticket editTic) {
        boolean flag = true;
        Ticket updateTicket = new Ticket(editTic);
        while (flag) {
            switch (userEditStatusSubMenu.show()) {
                case 1: {
                    updateTicket.setStatus(Status.TO_DO);
                    flag = false;
                    break;
                }
                case 2: {
                    updateTicket.setStatus(Status.IN_WORK);
                    flag = false;
                    break;
                }
                case 3: {
                    updateTicket.setStatus(Status.REVIEW);
                    flag = false;
                    break;
                }
                case 4: {
                    updateTicket.setStatus(Status.NEED_REFACTORING);
                    flag = false;
                    break;
                }
                case 5: {
                    updateTicket.setStatus(Status.DONE);
                    flag = false;
                    break;
                }
                case 0: {
                    flag = false;
                    break;
                }
                default: {
                    flag = true;
                    break;
                }
            }
        }
        //Service updateTicket
        printMessageMenu(currentUser.getUserName() + " edited status of ticket");
        System.out.println("Edited " + updateTicket);
        System.out.print("Do you want to save changes. (y/n): ");
        if (scanner.nextLine().equals("y")) {
            //Service updateTicket
            System.out.println("Change in status has been saved");
        }
    }

    private void showEditPriorityMenu(Ticket editTic) {
        boolean flag = true;
        Ticket updateTicket = new Ticket(editTic);
        while (flag) {
            switch (userEditPrioritySubMenu.show()) {
                case 1: {
                    updateTicket.setPriority(Priority.LOW);
                    flag = false;
                    break;
                }
                case 2: {
                    updateTicket.setPriority(Priority.MIDDLE);
                    flag = false;
                    break;
                }
                case 3: {
                    updateTicket.setPriority(Priority.HIGH);
                    flag = false;
                    break;
                }
                case 4: {
                    updateTicket.setPriority(Priority.EXTRA_HIGH);
                    flag = false;
                    break;
                }
                case 0: {
                    flag = false;
                    break;
                }
                default: {
                    flag = true;
                    break;
                }
            }
        }

        printMessageMenu(currentUser.getUserName() + " edited priority of ticket");
        System.out.println("Edited " + updateTicket);
        System.out.print("Do you want to save changes. (y/n): ");
        if (scanner.nextLine().equals("y")) {
            //Service updateTicket
            System.out.println("Change in priority has been saved");
        }
    }

    private void showEditEstimatedTime(Ticket editTic) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date estDate = null;
        while (estDate == null) {
            System.out.println("Enter new estimated deadline date in the format yyyy-MM-dd");
            System.out.println("For example, it is now " + format.format(new Date()));
            String line = scanner.nextLine();
            try {
                estDate = format.parse(line);
                Ticket updateTicket = new Ticket(editTic);
                updateTicket.setEstimatedTime(estDate);
                printMessageMenu(currentUser.getUserName() + " edited estimated date of ticket");
                System.out.println("Edited " + updateTicket);
                System.out.print("Do you want to save changes. (y/n): ");
                if (scanner.nextLine().equals("y")) {
                    //Service updateTicket
                    System.out.println("Change in estimated date has been saved");
                }
            } catch (ParseException e) {
                System.out.print("Sorry, that's not valid. Do you want to try again. (y/n): ");
                if (scanner.nextLine().equals("n")) return;
            }
        }
    }

    private void editReporterSubMenu(Ticket editTic) {
        String inputReporterUser = getReporterUser();
        if (inputReporterUser != null) {
            Ticket updateTicket = new Ticket(editTic);
            updateTicket.setReporter(inputReporterUser);
            //Service updateTicket
            printMessageMenu(currentUser.getUserName() + " edited reporter of ticket");
            System.out.println("Edited " + updateTicket);
            System.out.print("Do you want to save changes. (y/n): ");
            if (scanner.nextLine().equals("y")) {
                //Service updateTicket
                System.out.println("Change in reporter of ticket has been saved");
            }
        }
    }

    private String getReporterUser() {
        String inputReporterUser = null;
        boolean noValidNameUser = true;
        while (noValidNameUser) {
            System.out.print("Input name of reporter user: ");
            inputReporterUser = scanner.nextLine();
            if (clientUserService.loginIsBusy(inputReporterUser)) {
                noValidNameUser = false;
            } else {
                System.out.print("Sorry, you enter not valid name of user. Do you want to try again. (y/n): ");
                if (scanner.nextLine().equals("n")) return null;
            }
        }
        return inputReporterUser;
    }

    private void showDashboard() {
        System.out.println("Dashboard .... ");
        System.out.println("Sorry, the service is still under development");
    }

    private void systemOut() {
        System.out.println("********************");
        System.out.println("You exit from program. Bye-Bye!");
        System.exit(0);
    }

    private void printMessageMenu(String mes) {
        System.out.println("--------------------");
        System.out.println(mes);
    }

    private void printInvalidMessage() {
        System.out.println("********************");
        System.out.println("Incorrect data entry. Repeat again ");
    }
}
