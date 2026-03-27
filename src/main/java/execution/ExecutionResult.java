package execution;

public class ExecutionResult {
    private String output;
    private String error;
    private boolean timedOut;
    private boolean success;

    public ExecutionResult(String output, String error, boolean timedOut, boolean success) {
        this.output = output;
        this.error = error;
        this.timedOut = timedOut;
        this.success = success;
    }

    public String getOutput() { return output; }
    public String getError() { return error; }
    public boolean isTimedOut() { return timedOut; }
    public boolean isSuccess() { return success; }
}
