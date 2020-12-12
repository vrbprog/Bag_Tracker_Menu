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

    public int show(String header, String exit){
        System.out.println("********************");
        System.out.println(header);
        showItems(menu);
        System.out.println("--------------------");
        System.out.println(exit);
        System.out.println("********************");
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
