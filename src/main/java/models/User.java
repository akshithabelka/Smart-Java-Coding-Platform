package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String userId;
    private String username;
    private String passwordHash;  // hashed for security
    private List<Submission> submissions;

    public User(String userId, String username, String passwordHash) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.submissions = new ArrayList<>();
    }

    // Encapsulation: getters & setters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }

    public List<Submission> getSubmissions() { return submissions; }

    public void addSubmission(Submission submission) {
        submissions.add(submission);
    }

    @Override
    public String toString() {
        return "User: " + username + " (ID=" + userId + ")";
    }
}
