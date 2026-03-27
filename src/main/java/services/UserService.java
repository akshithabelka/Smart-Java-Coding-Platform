package services;

import exceptions.InvalidInputException;
import models.Admin;
import models.User;
import storage.DataStore;
import utils.IDGenerator;
import utils.PasswordHasher;
import utils.LoggerUtil;

public class UserService {

    public User registerUser(String username, String password, boolean isAdmin)
            throws InvalidInputException {

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new InvalidInputException("Username or password cannot be empty");
        }

        // 🔥 Check if username already exists
        for (User u : DataStore.userRepo.getAllUsers().values()) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                throw new InvalidInputException("Username already exists. Please choose another.");
            }
        }

        String hashed = PasswordHasher.hashPassword(password);
        String userId = "U" + IDGenerator.nextUserId();

        User user = isAdmin
                ? new Admin(userId, username, hashed)
                : new User(userId, username, hashed);

        DataStore.userRepo.addUser(user);
        LoggerUtil.log("New user registered: " + username);

        return user;
    }


    public User login(String username, String password) throws InvalidInputException {

        String hashed = PasswordHasher.hashPassword(password);

        for (User u : DataStore.userRepo.getAllUsers().values()) {
            if (u.getUsername().equals(username) &&
                u.getPasswordHash().equals(hashed)) {

                LoggerUtil.log("User logged in: " + username);
                return u;
            }
        }

        throw new InvalidInputException("Invalid username or password");
    }
}
