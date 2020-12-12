package controller;

import model.User;
import model.dao.UserDaoInFileImpl;
import model.dao.UserDaoInMemImpl;
import service.LoginUserService;
import service.UserService;
import viewConsole.*;
import model.dao.UserDao;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    private BaseMenu loginMenu;
    private BaseMenu userTopMenu;
    private UserDao userDao;
    private Scanner scanner;
    private UserService loginService;

    public Controller() {
        init();
    }

    private void init() {
        List<MenuItem> listLoginMenuItem = Arrays
                .stream(LoginMenuItem.values()).map(loginItem ->
                        new MenuItem(loginItem.toString()))
                .collect(Collectors.toList());
        loginMenu = new BaseMenu(listLoginMenuItem);

        List<MenuItem> listUserTopMenuItem = Arrays
                .stream(UserTopMenuItem.values()).map(loginItem ->
                        new MenuItem(loginItem.toString()))
                .collect(Collectors.toList());
        userTopMenu = new BaseMenu(listUserTopMenuItem);

        scanner = new Scanner(System.in);
        //userDao = new UserDaoInMemImpl();
        userDao = new UserDaoInFileImpl();
        loginService = new LoginUserService(userDao);
    }

    public void run() {

        boolean exitFlagTopMenu;
        do {
            while (!getChoiceUserLoginMenu()) {
                System.out.println("********************");
                System.out.println("Incorrect data entry. Repeat again ");
            }

            exitFlagTopMenu = getChoiceUserTopMenu();

//            String[] params = {"userTest", "12345"};
//            userDao.updateUser(userDao
//                    .getAll()
//                    .get(2), params);

//            userDao.deleteUser(userDao
//                    .getAll()
//                    .get(2));

            //userDao.deleteUser(new User("Alex","45"));

        } while (exitFlagTopMenu);

        //
        //
        //
        //

        System.exit(0);
    }

    private boolean getChoiceUserLoginMenu() {
        String headerLoginMenu = "Login menu";
        String exitLoginMenu = "0. Exit from program";
        switch (loginMenu.show(headerLoginMenu, exitLoginMenu)) {
            case 1: {
                System.out.println("Login ....");
                return loginSubMenu(scanner);
            }
            case 2: {
                System.out.println("Registration .... ");
                return registerSubMenu(scanner);
            }
            case 0: {
                System.exit(0);
            }
            default: {
                return false;
            }
        }
    }

    private boolean getChoiceUserTopMenu() {
        String headerUserTopMenu = "User top menu";
        String exitUserTopMenu = "0. Exit to login menu";
        switch (userTopMenu.show(headerUserTopMenu, exitUserTopMenu)) {
            case 1: {
                System.out.println("Creating ticket ....");
                return false;
            }
            case 2: {
                System.out.println("Editing ticket .... ");
                return false;
            }
            case 3: {
                System.out.println("My tickets list ....");
                return false;
            }
            case 4: {
                System.out.println("Dashboard .... ");
                return false;
            }
            case 0: {
                return true;
            }
            default: {
                System.out.println("Incorrect input data");
                return false;
            }
        }
    }

    private boolean loginSubMenu(Scanner scanner) {
        System.out.print("Input login: ");
        String login = scanner.nextLine();

        System.out.print("Input password: ");
        String password = scanner.nextLine();

        if (loginService.login(login, password)) {
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

        if (loginService.loginIsBusy(login)) {
            printMessageMenu("This login is busy");
            return false;
        }
        System.out.print("Enter password: ");
        password = scanner.nextLine();

        System.out.print("Confirm password: ");
        passwordConfirm = scanner.nextLine();

        if (!password.equals(passwordConfirm)) {
            printMessageMenu("Password confirmation failed");
            return false;
        }
        userDao.saveUser(new User(login, password));
        printMessageMenu("Hello. You are registered in system");
        return true;
    }

    private void printMessageMenu(String mes) {
        System.out.println("--------------------");
        System.out.println(mes);
    }
}
