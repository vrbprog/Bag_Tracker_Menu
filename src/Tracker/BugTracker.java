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
            }
            while (clientMenuService.runTopMenu()) {
            }
        }
    }
}
