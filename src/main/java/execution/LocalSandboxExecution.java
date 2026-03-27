package execution;

import exceptions.CompilationException;
import exceptions.ExecutionTimeoutException;
import exceptions.SandboxSecurityException;
import models.Problem;
import models.Submission;

import javax.tools.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.*;

public class LocalSandboxExecution implements ExecutionStrategy {

    private static final long TIMEOUT_MS = 3000;
    private static final String CLASS_NAME = "Main";

    /**
     * Execute for ALL testcases (keeps previous behaviour).
     */
    @Override
    public ExecutionResult execute(Submission submission, Problem problem)
            throws CompilationException, ExecutionTimeoutException, SandboxSecurityException, Exception {

        // Create sandbox directory and write + compile once
        String sandboxDirName = "sandbox/sub_" + submission.getSubmissionId();
        Path sandboxPath = Paths.get(sandboxDirName);
        Files.createDirectories(sandboxPath);

        Path javaFilePath = sandboxPath.resolve(CLASS_NAME + ".java");
        Files.writeString(javaFilePath, submission.getSubmittedCode());

        compile(javaFilePath);

        StringBuilder combinedOutput = new StringBuilder();
        StringBuilder combinedErrors = new StringBuilder();
        boolean overallSuccess = true;

        for (var testCase : problem.getTestCases()) {
            ExecutionResult res = runTestCase(sandboxPath, testCase.getInput());

            combinedOutput.append("Input: ").append(testCase.getInput()).append("\n");
            combinedOutput.append("Output:\n").append(res.getOutput() == null ? "" : res.getOutput()).append("\n");

            if (res.getError() != null && !res.getError().isBlank()) {
                combinedErrors.append(res.getError()).append("\n");
                overallSuccess = false;
            }
            if (res.isTimedOut()) {
                throw new ExecutionTimeoutException("Program exceeded time limit");
            }
        }

        return new ExecutionResult(
                combinedOutput.toString(),
                combinedErrors.toString(),
                false,
                overallSuccess
        );
    }

    /**
     * Execute for a single test input (NEW method).
     * This compiles once (if needed) and runs only that input, returning the ExecutionResult.
     */
    @Override
    public ExecutionResult execute(Submission submission, Problem problem, String input)
            throws CompilationException, ExecutionTimeoutException, SandboxSecurityException, Exception {

        // Prepare sandbox directory
        String sandboxDirName = "sandbox/sub_" + submission.getSubmissionId();
        Path sandboxPath = Paths.get(sandboxDirName);
        Files.createDirectories(sandboxPath);

        // Write source file (overwrite every run to keep things simple)
        Path javaFilePath = sandboxPath.resolve(CLASS_NAME + ".java");
        Files.writeString(javaFilePath, submission.getSubmittedCode());

        // Compile (throws CompilationException on failure)
        compile(javaFilePath);

        // Run single test
        ExecutionResult res = runTestCase(sandboxPath, input);
        return res;
    }

    // ---------- helper: compile ----------
    private void compile(Path javaFile) throws CompilationException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new CompilationException("Java Compiler not available (ensure JDK not JRE).");
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager =
                compiler.getStandardFileManager(diagnostics, null, null);

        Iterable<? extends JavaFileObject> units =
                fileManager.getJavaFileObjectsFromFiles(List.of(javaFile.toFile()));

        JavaCompiler.CompilationTask task =
                compiler.getTask(null, fileManager, diagnostics, null, null, units);

        boolean success = task.call();

        try {
            fileManager.close();
        } catch (IOException ignored) {}

        if (!success) {
            StringBuilder errors = new StringBuilder("Compilation Error:\n");
            for (Diagnostic<?> d : diagnostics.getDiagnostics()) {
                errors.append("Line ")
                        .append(d.getLineNumber())
                        .append(": ")
                        .append(d.getMessage(null))
                        .append("\n");
            }
            throw new CompilationException(errors.toString());
        }
    }

    // ---------- helper: run a single test (unchanged logic, but accessible publicly) ----------
    private ExecutionResult runTestCase(Path sandboxPath, String input)
            throws ExecutionTimeoutException, IOException {

        // Use platform JVM to run compiled class
        ProcessBuilder pb = new ProcessBuilder("java", CLASS_NAME);
        pb.directory(sandboxPath.toFile());
        pb.redirectErrorStream(true); // merge stderr into stdout

        Process process = pb.start();

        // feed input (if provided)
        if (input != null && !input.isBlank()) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(input);
                writer.newLine();
                writer.flush();
            } catch (IOException ignored) {}
        } else {
            // close stdin to avoid programs waiting for input
            try {
                process.getOutputStream().close();
            } catch (IOException ignored) {}
        }

        // capture output asynchronously
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> outputFuture = executor.submit(() -> readStream(process.getInputStream()));

        boolean finished;
        try {
            finished = process.waitFor(TIMEOUT_MS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new ExecutionTimeoutException("Execution interrupted");
        }

        if (!finished) {
            process.destroyForcibly();
            throw new ExecutionTimeoutException("Execution timed out");
        }

        String output = "";
        try {
            // small timeout to read remaining output
            output = outputFuture.get(200, TimeUnit.MILLISECONDS);
        } catch (Exception ignored) {}

        executor.shutdownNow();

        return new ExecutionResult(output, "", false, true);
    }

    // ---------- utility: read stream ----------
    private String readStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
