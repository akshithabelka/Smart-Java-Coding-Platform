package history;

import models.Submission;
import storage.DataStore;

import java.util.*;

public class SubmissionHistoryManager {

    // Mapping: problemId → list of versioned submissions
    private static Map<Integer, List<Submission>> history = new TreeMap<>();

    public static void addSubmission(Submission s) {
        history.putIfAbsent(s.getProblemId(), new ArrayList<>());
        List<Submission> list = history.get(s.getProblemId());

        // Assign version number
        int version = list.size() + 1;
        s.setVersionNumber(version);

        // Create a SNAPSHOT of this submission
        Submission snapshot = new Submission(
                s.getSubmissionId(),
                s.getProblemId(),
                s.getUserId(),
                s.getSubmittedCode()
        );

        snapshot.setVersionNumber(version);
        snapshot.setEvaluationResult(s.getEvaluationResult());
        snapshot.setProgramOutput(s.getProgramOutput());
        snapshot.setTimestamp(s.getTimestamp());

        // Add snapshot to history list
        list.add(snapshot);

        // Save updated repo
        DataStore.submissionRepo.save();
    }

    public static List<Submission> getHistoryForProblem(int problemId) {
        return history.getOrDefault(problemId, new ArrayList<>());
    }
}
