package viewConsole;

import java.util.List;
import java.util.Scanner;

public class BaseMenu implements Menu {

    protected List<MenuItem> menu;
    protected Scanner scanner;

    public BaseMenu(List<MenuItem> menu) {
        this.menu = menu;
        scanner = new Scanner(System.in);
    }

    public int show(){
        showHeaderMenu();
        showItems(menu);
        showBottomMenu();
        return getUserChoice();
    }

    public void showHeaderMenu(){
        System.out.println("********************");
        System.out.println(menu.get(0).getNameItem());
    }

    public void showBottomMenu(){
        System.out.println("--------------------");
        System.out.println(menu.get(menu.size()-1).getNameItem());
        System.out.println("********************");
    }

    private int getUserChoice(){
        System.out.print("Make your choice: ");
        String choice =  scanner.nextLine();
        if(isNumeric(choice)) return Integer.parseInt(choice);
        else return -1;
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
