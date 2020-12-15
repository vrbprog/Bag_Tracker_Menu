package controller;

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
        loginMenu = createLoginMenu();
        userTopMenu = createUserTopMenu();
        userEditSubMenu = createUserEditSubMenu();
        userEditStatusSubMenu = createUserEditStatusSubMenu();
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

    private BaseMenu createUserEditStatusSubMenu() {
        List<MenuItem> listUserEditSubMenuItem = new ArrayList<>();
        listUserEditSubMenuItem.add(new HeaderMenuItem("Edit status menu"));
        listUserEditSubMenuItem.addAll(Arrays
                .stream(Status.values()).map(loginItem ->
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
            try {
                if (clientUserService.userRegistration(currentUser)) {
                    printMessageMenu("Hello " + login + ". You are registered in system");
                    return false;
                }else{
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
                if(currentUser.getUserName().equals(editTicket.getAssignee())) {
                    nameTickerNoPresent = false;
                    System.out.print("Editing ");
                    System.out.println(editTicket);
                }else{
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
                    break;
                }
                case 4: {
                    System.out.println("Editing estimated time ....");
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

    private void showEditStatusMenu(Ticket editTic){
        boolean flag = true;
        Ticket updateTicket = new Ticket(editTic);
        while(flag) {
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
        printMessageMenu("For return in menu press Enter");
        scanner.nextLine();
    }

   private void editReporterSubMenu(Ticket editTic){
       String inputReporterUser = getReporterUser();
       if(inputReporterUser != null){
           Ticket updateTicket = new Ticket(editTic);
           updateTicket.setReporter(inputReporterUser);
           //Service updateTicket
           printMessageMenu(currentUser.getUserName() + " edited reporter of ticket");
           System.out.println("Edited " + updateTicket);
           printMessageMenu("For return in menu press Enter");
           scanner.nextLine();
       }
   }

   private String getReporterUser(){
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
