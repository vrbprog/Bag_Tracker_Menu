package Tracker;

import service.*;

public class BugTracker {

    private final MenuService clientMenuService;

    public BugTracker() {
        clientMenuService = new ClientMenuService();
    }

    public void run() {
        while (true) {
            while (clientMenuService.runLoginMenu()) {
                System.out.println("********************");
                System.out.println("Incorrect data entry. Repeat again ");
            }
            while (clientMenuService.runTopMenu()) {
                System.out.println("********************");
            }
        }
    }
}
