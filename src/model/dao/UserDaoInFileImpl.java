package model.dao;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserDaoInFileImpl implements UserDao {
    private final List<User> users = new ArrayList<>();
    private final String nameFile;
    private final String regex = ":";

    public UserDaoInFileImpl() {
        nameFile = "src" + File.separator +
                "resources" + File.separator +
                "userDB.txt";
        loadUsersFromFile(nameFile);
    }

    @Override
    public void saveUser(User user) {
        writeUserToFile(user);
        users.add(user);
    }

    @Override
    public void deleteUser(User user) {
        deleteUserFromFile(user);
        users.remove(user);
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public User getUserByName(String userName) {
        Optional<User> optionalUser = users.stream()
                .filter(user -> user.getUserName()
                        .equals(userName)).findFirst();
        return optionalUser.orElseGet(User::new);
    }

    @Override
    public void updateUser(User user, String[] params) {

    }

    private void addUserFromLine(String line) {
        String[] array = line.split(regex);
        users.add(new User(array[0], array[1]));
    }

    private void loadUsersFromFile(String nameFile) {
        try (
                FileReader fileReader = new FileReader(nameFile);
                BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                addUserFromLine(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeUserToFile(User user) {
        try (
                PrintWriter out = new PrintWriter(new FileWriter(nameFile, true))
        ) {
            out.println(user.getUserName() + regex + user.getPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteUserFromFile(User user) {
        Scanner sc = null;
        try {
            sc = new Scanner(new File(nameFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String fileContents;
        StringBuilder buffer = new StringBuilder();
        if(sc != null) {
            while (sc.hasNextLine()) {
                buffer.append(sc.nextLine()).append(System.lineSeparator());
            }
            fileContents = buffer.toString();
            sc.close();
            fileContents = fileContents.replaceAll(user.getUserName() + regex +
                    user.getPassword() + System.lineSeparator(), "");
            FileWriter writer;
            try {
                writer = new FileWriter(nameFile);
                writer.append(fileContents);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
