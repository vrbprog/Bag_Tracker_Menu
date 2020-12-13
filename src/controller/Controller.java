package controller;

import model.Ticket;
import model.User;
import model.dao.TicketDao;
import model.dao.TicketDaoInMemImpl;
import model.dao.UserDaoInFileImpl;
import service.ClientUserService;
import service.UserService;
import viewConsole.*;
import model.dao.UserDao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    private BaseMenu loginMenu;
    private BaseMenu userTopMenu;
    private final Scanner scanner;
    private final UserService clientUserService;
    private User currentUser;
    private final List<MenuItem> listLoginMenuItem = new ArrayList<>();
    private final List<MenuItem> listUserTopMenuItem = new ArrayList<>();

    public Controller() {
        UserDao userDao = new UserDaoInFileImpl();
        //userDao = new UserDaoInMemImpl();
        TicketDao ticketDao = new TicketDaoInMemImpl();
        clientUserService = new ClientUserService(userDao);
        createLoginMenu();
        createUserTopMenu();
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            while (!getChoiceUserLoginMenu()) {
                printInvalidMessage();
            }
            while (!getChoiceUserTopMenu()) {
                printInvalidMessage();
            }
        }

        //
        //
        //
        //
        //            String[] params = {"userTest", "88888"};
//            userDao.updateUser(userDao
//                    .getAll()
//                    .get(1), params);
//            userDao.deleteUser(userDao
//                    .getAll()
//                    .get(3));


        //System.exit(0);
    }

    private void createLoginMenu() {
        listLoginMenuItem.add(new HeaderMenuItem("Login menu"));
        listLoginMenuItem.addAll(Arrays
                .stream(LoginMenuItem.values()).map(loginItem ->
                        new MenuItem(loginItem.toString()))
                .collect(Collectors.toList()));
        listLoginMenuItem.add(new BottomMenuItem("0. Exit from program"));
        loginMenu = new BaseMenu(listLoginMenuItem);
    }

    private void createUserTopMenu() {
        listUserTopMenuItem.add(new HeaderMenuItem("User top menu"));
        listUserTopMenuItem.addAll(Arrays
                .stream(UserTopMenuItem.values()).map(loginItem ->
                        new MenuItem(loginItem.toString()))
                .collect(Collectors.toList()));
        listUserTopMenuItem.add(new BottomMenuItem("0. Exit from program"));
        userTopMenu = new BaseMenu(listUserTopMenuItem);
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
                return false;
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
                return true;
            }
            case 3: {
                System.out.println("My tickets list ....");
                return true;
            }
            case 4: {
                System.out.println("Dashboard .... ");
                return true;
            }
            case 5: {
                return true;
            }
            case 0: {
                systemOut();
            }
            default: {
                return false;
            }
        }
    }

    private boolean loginSubMenu(Scanner scanner) {
        System.out.print("Input login: ");
        String login = scanner.nextLine();

        System.out.print("Input password: ");
        String password = scanner.nextLine();

        if (clientUserService.login(login, password)) {
            currentUser = new User(login, password);
            printMessageMenu("Hello. You are login in system");
            return true;
        } else {
            printMessageMenu("Wrong username/password");
            return false;
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
            return false;
        }
        System.out.print("Enter password: ");
        password = scanner.nextLine();

        System.out.print("Confirm password: ");
        passwordConfirm = scanner.nextLine();

        if (!password.equals(passwordConfirm)) {
            printMessageMenu("Confirm of password failed");
            return false;
        }
        currentUser = new User(login, password);
        if(clientUserService.userRegistration(currentUser)) {
            printMessageMenu("Hello. You are registered in system");
            return true;
        }else{
            printMessageMenu("Registration failed!");
            return false;
        }
    }

    private boolean createTicketSubMenu(Scanner scanner) {
        System.out.print("Input name of ticket: ");
        String nameTicket = scanner.nextLine();

        System.out.print("Input description of ticket: ");
        String description = scanner.nextLine();

        String reporterUser = null;
        boolean noNameUser = true;
        while (noNameUser) {
            System.out.print("Input name of reporter user: ");
            reporterUser = scanner.nextLine();
            if (clientUserService.loginIsBusy(reporterUser)) {
                noNameUser = false;
            } else {
                System.out.print("Sorry, you enter not valid name of user. Do you want to try again. (y/n): ");
                if (scanner.nextLine().equals("n")) return false;
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
                if (scanner.nextLine().equals("n")) return false;
            }
        }
        System.out.println(format.format(estDate));

        Ticket newTicket = new Ticket(nameTicket, description,
                currentUser.getUserName(),
                reporterUser, new Date(), estDate);
        printMessageMenu("You create ticket:");
        System.out.println(newTicket);
        printMessageMenu("For return in menu press Enter");
        scanner.nextLine();
        return false;

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
