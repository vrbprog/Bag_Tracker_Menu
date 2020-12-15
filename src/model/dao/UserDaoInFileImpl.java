package model.dao;

import model.User;

import java.io.*;
import java.util.*;

public class UserDaoInFileImpl implements UserDao {
    private final List<User> users = new ArrayList<>();
    private final String nameFile;
    private final String regex = ":";

    public UserDaoInFileImpl() throws FileNotFoundException {
        nameFile = "src" + File.separator +
                "resources" + File.separator +
                "userDB.txt";
        loadUsersFromFile(nameFile);
    }

    @Override
    public void saveUser(User user) throws IOException {
        users.add(user);
        writeUserToFile(user);
    }

    @Override
    public void deleteUser(User user) throws IOException {
        updateUserFromFile(user,"");
        users.remove(user);
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public User getUserByName(String userName) {
        return users.stream()
                .filter(user -> user.getUserName()
                        .equals(userName)).findFirst().orElse(null);
    }

    @Override
    public void updateUser(User user, String[] params) throws IOException {
        User newUser = new User();
        newUser.setUserName(Objects.requireNonNull(
                params[0], "Name cannot be null"));
        newUser.setPassword(Objects.requireNonNull(
                params[1], "Password cannot be null"));
        String stringNewUser = newUser.getUserName() + regex +
                newUser.getPassword() + System.lineSeparator();
        updateUserFromFile(user,stringNewUser);
        users.set(users.indexOf(user),newUser);
    }

    private void addUserFromLine(String line) {
        String[] array = line.split(regex);
        users.add(new User(array[0], array[1]));
    }

    private void loadUsersFromFile(String nameFile) throws FileNotFoundException{

        try (
             FileReader fileReader = new FileReader(nameFile);
             BufferedReader reader = new BufferedReader(fileReader)
        ) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                addUserFromLine(currentLine);
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeUserToFile(User user) throws IOException {
        File file = new File(nameFile);
        if (file.exists()) {
            try (
                    PrintWriter out = new PrintWriter(new FileWriter(file, true))
            ) {
                out.println(user.getUserName() + regex + user.getPassword());
            } catch (IOException e) {
                throw e;
            }
        }else{
            throw new IOException("File not found");
        }
    }

    private void updateUserFromFile(User user, String update) throws IOException {
        Scanner sc;
        try {
            sc = new Scanner(new File(nameFile));
        } catch (FileNotFoundException e) {
            throw e;
        }
        String fileContents;
        StringBuilder buffer = new StringBuilder();

        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()).append(System.lineSeparator());
        }
        fileContents = buffer.toString();
        sc.close();
        fileContents = fileContents.replaceAll(user.getUserName() + regex +
                user.getPassword() + System.lineSeparator(), update);
        FileWriter writer;
        try {
            writer = new FileWriter(nameFile);
            writer.append(fileContents);
            writer.flush();
        } catch (IOException e) {
            throw e;
        }
    }
}
