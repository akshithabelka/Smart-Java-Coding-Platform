package services;

import exceptions.InvalidInputException;
import models.Difficulty;
import models.Problem;
import models.TestCase;
import storage.DataStore;
import utils.IDGenerator;
import utils.LoggerUtil;

import java.util.Map;

public class ProblemService {

    public Problem createProblem(String title, String description, Difficulty difficulty)
            throws InvalidInputException {

        if (title == null || title.isBlank())
            throw new InvalidInputException("Title cannot be empty");

        int id = IDGenerator.nextProblemId();
        Problem problem = new Problem(id, title, description, difficulty);

        DataStore.problemRepo.addProblem(problem);
        LoggerUtil.log("New problem added: " + title);

        return problem;
    }

    public void addTestCase(int problemId, String input, String expectedOutput, boolean hidden)
            throws InvalidInputException {

        Problem problem = DataStore.problemRepo.getProblem(problemId);

        if (problem == null) {
            throw new InvalidInputException("Invalid problem ID");
        }

        TestCase testCase = new TestCase(input, expectedOutput, hidden);
        problem.addTestCase(testCase);

        DataStore.problemRepo.save();
        LoggerUtil.log("Testcase added to problem ID: " + problemId);
    }

    public Map<Integer, Problem> getAllProblems() {
        return DataStore.problemRepo.getAllProblems();
    }

    public Problem getProblem(int problemId) throws InvalidInputException {
        Problem p = DataStore.problemRepo.getProblem(problemId);
        if (p == null) throw new InvalidInputException("Problem does not exist");
        return p;
    }
}
