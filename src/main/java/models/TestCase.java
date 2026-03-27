package models;

import java.io.Serializable;

public class TestCase implements Serializable {
    private String input;
    private String expectedOutput;
    private boolean hidden; // for hidden test cases

    public TestCase(String input, String expectedOutput, boolean hidden) {
        this.input = input;
        this.expectedOutput = expectedOutput;
        this.hidden = hidden;
    }

    public String getInput() { return input; }
    public String getExpectedOutput() { return expectedOutput; }
    public boolean isHidden() { return hidden; }
}
