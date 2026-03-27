package storage;

import models.Submission;
import utils.LoggerUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SubmissionRepository {

    private static final String FILE_PATH =
            System.getProperty("user.home") + "/coding-platform/submissions.ser";

    private List<Submission> submissions = new ArrayList<>();

    public synchronized void addSubmission(Submission submission) {
        submissions.add(submission);
        save();
    }

    public List<Submission> getAllSubmissions() {
        return submissions;
    }

    public List<Submission> getSubmissionsByUser(String userId) {
        List<Submission> result = new ArrayList<>();
        for (Submission s : submissions) {
            if (s.getUserId().equals(userId)) result.add(s);
        }
        return result;
    }

    public List<Submission> getSubmissionsByProblem(int problemId) {
        List<Submission> result = new ArrayList<>();
        for (Submission s : submissions) {
            if (s.getProblemId() == problemId) result.add(s);
        }
        return result;
    }

    public void save() {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs(); // ensure directory exists

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(submissions); // or problems/submissions
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
            submissions = (List<Submission>) ois.readObject();
        } catch (Exception e) {
            LoggerUtil.logError("Error loading Submissions", e);
        }
    }
}
