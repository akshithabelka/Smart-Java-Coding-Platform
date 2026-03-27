package execution;

import exceptions.CompilationException;
import exceptions.ExecutionTimeoutException;
import exceptions.SandboxSecurityException;
import models.Problem;
import models.Submission;

public interface ExecutionStrategy {
    /**
     * Execute the submission against all testcases for the problem.
     * (Older API kept for compatibility)
     */
    ExecutionResult execute(Submission submission, Problem problem)
            throws CompilationException, ExecutionTimeoutException, SandboxSecurityException, Exception;

    /**
     * Execute the submission for a single test case input and return the result for that run.
     * (New API used by SubmissionService to evaluate testcases individually.)
     */
    ExecutionResult execute(Submission submission, Problem problem, String input)
            throws CompilationException, ExecutionTimeoutException, SandboxSecurityException, Exception;
}
