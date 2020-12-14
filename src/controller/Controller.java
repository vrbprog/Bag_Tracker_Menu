package controller;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    private final BaseMenu loginMenu;
    private final BaseMenu userTopMenu;
    private final BaseMenu userEditSubMenu;
    private final Scanner scanner;
    private final UserService clientUserService;
    private final TicketService clientTicketService;
    private User currentUser;

    public Controller() {
        UserDao userDao = new UserDaoInFileImpl();
        //userDao = new UserDaoInMemImpl();
        TicketDao ticketDao = new TicketDaoInMemImpl();
        clientUserService = new ClientUserService(userDao);
        clientTicketService = new ClientTicketService(ticketDao);
        loginMenu = createLoginMenu();
        userTopMenu = createUserTopMenu();
        userEditSubMenu = createUserEditSubMenu();
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            while (getChoiceUserLoginMenu()) {
                printInvalidMessage();
            }
            while (getChoiceUserTopMenu()) {
                System.out.println("********************");
            }
        }
    }

    private boolean getChoiceUserLoginMenu() {
        switch (loginMenu.show()) {
            case 1: {
                System.out.println("Login ....");
                return loginSubMenu(scanner);
            }
            case 2: {
                System.out.println("Registration .... ");
                return registerSubMenu(scanner);
            }
            case 0: {
                systemOut();
            }
            default: {
                return true;
            }
        }
    }

    private boolean getChoiceUserTopMenu() {
        switch (userTopMenu.show()) {
            case 1: {
                System.out.println("Creating ticket ....");
                return createTicketSubMenu(scanner);
            }
            case 2: {
                System.out.println("Editing ticket .... ");
                return editTicket(scanner);
            }
            case 3: {
                System.out.println("My tickets list ....");
                showUserTickets();
                return true;
            }
            case 4: {
                showDashboard();
                return true;
            }
            case 5: {
                return false;
            }
            case 0: {
                systemOut();
            }
            default: {
                return true;
            }
        }
    }

    private BaseMenu createLoginMenu() {
        List<MenuItem> listLoginMenuItem = new ArrayList<>();
        listLoginMenuItem.add(new HeaderMenuItem("Login menu"));
        listLoginMenuItem.addAll(Arrays
                .stream(LoginMenuItem.values()).map(loginItem ->
                        new MenuItem(loginItem.toString()))
                .collect(Collectors.toList()));
        listLoginMenuItem.add(new BottomMenuItem("0. Exit from program"));
        return new BaseMenu(listLoginMenuItem);
    }

    private BaseMenu createUserTopMenu() {
        List<MenuItem> listUserTopMenuItem = new ArrayList<>();
        listUserTopMenuItem.add(new HeaderMenuItem("User top menu"));
        listUserTopMenuItem.addAll(Arrays
                .stream(UserTopMenuItem.values()).map(loginItem ->
                        new MenuItem(loginItem.toString()))
                .collect(Collectors.toList()));
        listUserTopMenuItem.add(new BottomMenuItem("0. Exit from program"));
        return new BaseMenu(listUserTopMenuItem);
    }

    private BaseMenu createUserEditSubMenu() {
        List<MenuItem> listUserEditSubMenuItem = new ArrayList<>();
        listUserEditSubMenuItem.add(new HeaderMenuItem("User edit menu"));
        listUserEditSubMenuItem.addAll(Arrays
                .stream(UserEditSubMenuItem.values()).map(loginItem ->
                        new MenuItem(loginItem.toString()))
                .collect(Collectors.toList()));
        listUserEditSubMenuItem.add(new BottomMenuItem("0. Exit from edit menu"));
        return new BaseMenu(listUserEditSubMenuItem);
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
        if (clientUserService.userRegistration(currentUser)) {
            printMessageMenu("Hello " + login + ". You are registered in system");
            return false;
        } else {
            printMessageMenu("Registration failed!");
            return true;
        }
    }

    private boolean createTicketSubMenu(Scanner scanner) {
        System.out.print("Input name of ticket: ");
        String nameTicket = scanner.nextLine();

        System.out.print("Input description of ticket: ");
        String description = scanner.nextLine();

        String reporterUser = null;
        boolean noValidNameUser = true;
        while (noValidNameUser) {
            System.out.print("Input name of reporter user: ");
            reporterUser = scanner.nextLine();
            if (clientUserService.loginIsBusy(reporterUser)) {
                noValidNameUser = false;
            } else {
                System.out.print("Sorry, you enter not valid name of user. Do you want to try again. (y/n): ");
                if (scanner.nextLine().equals("n")) return true;
            }
        }

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
        Ticket editTicket;
        boolean nameTickerNoPresent = true;
        while (nameTickerNoPresent) {
            System.out.print("Input name of editing ticket: ");
            String ticketName = scanner.nextLine();
            editTicket = clientTicketService.getTicketByName(ticketName);
            if (editTicket != null) {
                nameTickerNoPresent = false;
                System.out.print("Editing ticket: ");
                System.out.println(editTicket);
            } else {
                System.out.print("Sorry, that's not valid name of ticket. Do you want to try again. (y/n): ");
                if (scanner.nextLine().equals("n")) return true;
            }
        }

        printMessageMenu("You can edit fields: reporter, status and priority");
        boolean isEditingTicket = true;
        while (isEditingTicket) {
            switch (userEditSubMenu.show()) {
                case 1: {
                    System.out.println("Editing reporter ....");
                    break;
                }
                case 2: {
                    System.out.println("Editing status .... ");
                    break;
                }
                case 3: {
                    System.out.println("Editing priority ....");
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
