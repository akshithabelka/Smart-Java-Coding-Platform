package services;

import evaluation.EvaluationEngine;
import exceptions.*;
import execution.*;
import history.SubmissionHistoryManager;
import models.Problem;
import models.Submission;
import models.TestCase;
import models.User;
import storage.DataStore;
import threading.EvaluationThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SubmissionService {

    private ExecutionStrategy executor = new LocalSandboxExecution();
    private EvaluationEngine evaluator = new EvaluationEngine();

    public Submission submitAndEvaluate(User user, Problem problem, String code)
            throws InvalidInputException {

        if (code == null || code.isBlank()) {
            throw new InvalidInputException("Code submission cannot be empty");
        }

        // Create new submission
        Submission submission = new Submission(
                utils.IDGenerator.nextSubmissionId(),
                problem.getProblemId(),
                user.getUserId(),
                code
        );

        DataStore.submissionRepo.addSubmission(submission);
        user.addSubmission(submission);

        List<String> perTestOutputs = new ArrayList<>();
        boolean allPassed = true;
        StringBuilder combinedOutput = new StringBuilder();

        try {
            // Run each testcase independently
            for (TestCase tc : problem.getTestCases()) {

                Future<ExecutionResult> future =
                        EvaluationThreadPool.submitTask(
                                () -> executor.execute(submission, problem, tc.getInput()));

                ExecutionResult result = future.get();

                String actualOutput = (result.getOutput() == null ? "" : result.getOutput().trim());
                perTestOutputs.add(actualOutput);

                // Add to combined output (for UI display)
                combinedOutput.append("Input: ").append(tc.getInput()).append("\n")
                        .append("Output:\n").append(actualOutput).append("\n\n");

                // Compare expected vs actual
                boolean passed = evaluator.compare(tc.getExpectedOutput(), actualOutput);
                if (!passed) {
                    allPassed = false;
                }
            }

            submission.setPerTestOutputs(perTestOutputs);
            submission.setProgramOutput(combinedOutput.toString());
            submission.setEvaluationResult(allPassed ? "PASS" : "FAIL");

        } catch (ExecutionException e) {

            Throwable cause = e.getCause();

            if (cause instanceof CompilationException) {
                submission.setEvaluationResult("COMPILATION ERROR");
                submission.setProgramOutput(cause.getMessage());
            }
            else if (cause instanceof ExecutionTimeoutException) {
                submission.setEvaluationResult("TIMEOUT");
                submission.setProgramOutput("Your code took too long to execute.");
            }
            else if (cause instanceof SandboxSecurityException) {
                submission.setEvaluationResult("SECURITY VIOLATION");
                submission.setProgramOutput("Your code attempted a restricted operation.");
            }
            else {
                submission.setEvaluationResult("RUNTIME ERROR");
                submission.setProgramOutput("Error: " + cause.getMessage());
            }

        } catch (InterruptedException e) {
            submission.setEvaluationResult("INTERRUPTED");
            submission.setProgramOutput("Execution was interrupted.");
        }

        // Save history version
        SubmissionHistoryManager.addSubmission(submission);
        DataStore.submissionRepo.save();

        return submission;
    }
}
