package controller;

import model.dao.UserDaoInMemImpl;
import service.LoginUserService;
import service.UserService;
import viewConsole.LoginMenu;
import viewConsole.MenuItem;
import viewConsole.UserTopMenu;
import model.dao.UserDao;
import model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Controller {

    private LoginMenu loginMenu;
    private UserTopMenu userTopMenu;
    private UserDao userDao;
    private Scanner scanner;
    private UserService loginService;

    public Controller() {
        init();
    }

    private void init(){
        List<MenuItem> listLoginMenuItem = new ArrayList<>(Arrays.asList(
                new MenuItem("Login"),
                new MenuItem("Registration")));
        loginMenu = new LoginMenu(listLoginMenuItem);

        List<MenuItem> listUserTopMenuItem = new ArrayList<>(Arrays.asList(
                new MenuItem("Create ticket"),
                new MenuItem("Edit ticket"),
                new MenuItem("My tickets list"),
                new MenuItem("My dashboard")));
        userTopMenu = new UserTopMenu(listUserTopMenuItem);

        scanner = new Scanner(System.in);
        UserDao userDao = new UserDaoInMemImpl();
        loginService = new LoginUserService(userDao);
    }

    public void run(){

        boolean exitFlagTopMenu;
        do {
            while (!getChoiceUserLoginMenu()) {
                System.out.println("********************");
                System.out.println("Incorrect data entry. Repeat again ");
            }

            exitFlagTopMenu = getChoiceUserTopMenu();
        }while (exitFlagTopMenu);

        //
        //
        //
        //

        System.exit(0);
    }

    private boolean getChoiceUserLoginMenu(){
        switch (loginMenu.show()){
            case 1:{
                System.out.println("Login ....");
                return loginSubMenu(scanner);
            }
            case 2:{
                System.out.println("Registration .... ");
                return registerSubMenu(scanner);
            }
            case 0:{
                System.exit(0);
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

    private boolean loginSubMenu(Scanner scanner)
    {
        System.out.print("Input login: ");
        String login =  scanner.nextLine();

        System.out.print("Input password: ");
        String password =  scanner.nextLine();

        if(loginService.login(login, password)) {
            System.out.println("######################");
            System.out.println("Hello. You are login in system");
            return true;
        }
        else {
            System.out.println("--------------------");
            System.out.println("Wrong username/password");
            return false;
        }
    }

    private boolean registerSubMenu(Scanner scanner)
    {
        String login;
        String password;
        String passwordConfirm;

        System.out.print("Enter login: ");
        login = scanner.nextLine();

        if(loginService.loginIsBusy(login)) {
            System.out.println("--------------------");
            System.out.println("This login is busy");
            return false;
        }
            System.out.print("Enter password: ");
            password = scanner.nextLine();

            System.out.print("Confirm password: ");
            passwordConfirm = scanner.nextLine();

            if(!password.equals(passwordConfirm)) {
                System.out.println("--------------------");
                System.out.println("Password confirmation failed");
                return false;
            }
        System.out.println("--------------------");
        System.out.println("Hello. You are registered in system");
        return true;
    }
}
