package viewConsole;

public class MenuItem {

    private final String nameItem;
    private final boolean bodyItemMenu;
    private final boolean headerItemMenu;
    private final boolean bottomItemMenu;

    public MenuItem(String nameItem) {
        this.nameItem = nameItem;
        bodyItemMenu = true;
        headerItemMenu = false;
        bottomItemMenu = false;
    }

    public MenuItem(String nameItem, boolean bodyItemMenu, boolean headerItemMenu, boolean bottomItemMenu) {
        this.nameItem = nameItem;
        this.bodyItemMenu = bodyItemMenu;
        this.headerItemMenu = headerItemMenu;
        this.bottomItemMenu = bottomItemMenu;
    }


    public String getNameItem() {
        return nameItem;
    }

    public boolean isItemBodyMenu(){
        return bodyItemMenu;
    }

    public boolean isHeaderItemMenu() {
        return headerItemMenu;
    }

    public boolean isBottomItemMenu() {
        return bottomItemMenu;
    }
}
