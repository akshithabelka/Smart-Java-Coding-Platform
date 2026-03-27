package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Problem implements Serializable {

    private int problemId;
    private String title;
    private String description;
    private Difficulty difficulty;
    private List<TestCase> testCases;

    public Problem(int problemId, String title, String description, Difficulty difficulty) {
        this.problemId = problemId;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.testCases = new ArrayList<>();
    }

    public int getProblemId() { return problemId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Difficulty getDifficulty() { return difficulty; }
    public List<TestCase> getTestCases() { return testCases; }

    public void addTestCase(TestCase tc) {
        testCases.add(tc);
    }
}
