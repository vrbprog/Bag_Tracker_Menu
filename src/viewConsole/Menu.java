package viewConsole;

import java.util.List;

public interface Menu {
    int show();

    //void exit();

    default void showItems(List<MenuItem> items) {
        int i = 1;
        for (MenuItem item : items) {
            System.out.println("--------------------");
            System.out.println((i++) + ". " + item.getNameItem());
        }
    }
}
