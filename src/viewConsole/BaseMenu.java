package viewConsole;

import java.util.List;
import java.util.Scanner;

public abstract class BaseMenu implements Menu{

    protected List<MenuItem> menu;
    protected Scanner scanner;

    public BaseMenu(List<MenuItem> menu) {
        this.menu = menu;
        scanner = new Scanner(System.in);
    }
    public abstract int show();

    //public abstract void exit();

    public void addNewItem(MenuItem item){
         menu.add(item);
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
