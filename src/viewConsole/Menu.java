package viewConsole;

import java.util.List;

public interface Menu {
    int show();

    default void showItems(List<MenuItem> items) {
        int i = 1;
        for (MenuItem item : items) {
            if(item.isItemBodyMenu()) {
                System.out.println("--------------------");
                System.out.println((i++) + ". " + item.getNameItem());
            }
        }
    }
}
