package storage;

import models.Problem;
import utils.LoggerUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ProblemRepository {

    private static final String FILE_PATH =
            System.getProperty("user.home") + "/coding-platform/problems.ser";

    private Map<Integer, Problem> problems = new HashMap<>();

    public synchronized void addProblem(Problem problem) {
        problems.put(problem.getProblemId(), problem);
        save();
    }

    public Problem getProblem(int id) {
        return problems.get(id);
    }

    public Map<Integer, Problem> getAllProblems() {
        return problems;
    }

    public void save() {
        try {
            File file = new File(FILE_PATH);
            file.getParentFile().mkdirs(); // ensure directory exists

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(problems); // or problems/submissions
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
            problems = (Map<Integer, Problem>) ois.readObject();
        } catch (Exception e) {
            LoggerUtil.logError("Error loading Problems", e);
        }
    }
}
