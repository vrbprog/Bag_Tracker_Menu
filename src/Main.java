import controller.Controller;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {

        try {
            Controller controller = new Controller();
            controller.run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error. The data file is not available, the application will be closed");
        }

    }
}

