package threading;

import execution.ExecutionResult;
import java.util.concurrent.*;

public class EvaluationThreadPool {

    private static final int POOL_SIZE = 10;

    private static ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

    public static Future<ExecutionResult> submitTask(Callable<ExecutionResult> task) {
        return executor.submit(task);
    }

    public static void shutdown() {
        executor.shutdown();
    }
}
