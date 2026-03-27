package storage;

import models.User;
import utils.LoggerUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private static final String FILE_PATH =
            System.getProperty("user.home") + "/coding-platform/users.ser";

    private Map<String, User> users = new HashMap<>();

    public synchronized void addUser(User user) {
        users.put(user.getUserId(), user);
        save();
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public Map<String, User> getAllUsers() {
        return users;
    }

    public void save() {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs(); // ensure directory exists

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(users); // or problems/submissions
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.logError("Error saving data", e);
        }
    }


    @SuppressWarnings("unchecked")
    public void load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            users = (Map<String, User>) ois.readObject();
        } catch (Exception e) {
            LoggerUtil.logError("Error loading Users", e);
        }
    }
}
