package storage;

public class DataStore {

    public static UserRepository userRepo = new UserRepository();
    public static ProblemRepository problemRepo = new ProblemRepository();
    public static SubmissionRepository submissionRepo = new SubmissionRepository();

    public static void loadAll() {
        userRepo.load();
        problemRepo.load();
        submissionRepo.load();
    }

    public static void saveAll() {
        userRepo.save();
        problemRepo.save();
        submissionRepo.save();
    }
}
