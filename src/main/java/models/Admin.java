package models;

public class Admin extends User {

    public Admin(String userId, String username, String passwordHash) {
        super(userId, username, passwordHash);
    }

    @Override
    public String toString() {
        return "Admin: " + getUsername();
    }
}
