package models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Submission implements Serializable {

    private int submissionId;
    private int problemId;
    private String userId;
    private String submittedCode;
    private LocalDateTime timestamp;
    private int versionNumber;
    private String evaluationResult;
    private String programOutput;

    // NEW: per-testcase outputs
    private List<String> perTestOutputs = new ArrayList<>();

    public Submission(int submissionId, int problemId, String userId, String submittedCode) {
        this.submissionId = submissionId;
        this.problemId = problemId;
        this.userId = userId;
        this.submittedCode = submittedCode;
        this.timestamp = LocalDateTime.now();
    }

    // ------------------- GETTERS -------------------
    public int getSubmissionId() { return submissionId; }
    public int getProblemId() { return problemId; }
    public String getUserId() { return userId; }
    public String getSubmittedCode() { return submittedCode; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getVersionNumber() { return versionNumber; }
    public String getEvaluationResult() { return evaluationResult; }
    public String getProgramOutput() { return programOutput; }
    public List<String> getPerTestOutputs() { return perTestOutputs; }


    // ------------------- SETTERS -------------------
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public void setEvaluationResult(String evaluationResult) {
        this.evaluationResult = evaluationResult;
    }

    public void setProgramOutput(String programOutput) {
        this.programOutput = programOutput;
    }

    public void setPerTestOutputs(List<String> outputs) {
        this.perTestOutputs = outputs;
    }

    public void addPerTestOutput(String output) {
        this.perTestOutputs.add(output);
    }
}
