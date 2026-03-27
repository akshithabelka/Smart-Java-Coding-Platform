package evaluation;

public class FeedbackGenerator {

    public static String create(String expected, String actual) {

        String normExpected = OutputNormalizer.normalize(expected);
        String normActual = OutputNormalizer.normalize(actual);

        if (normExpected.equals(normActual)) {
            return "Correct! Your logic is working.";
        }

        StringBuilder fb = new StringBuilder();
        fb.append("Your solution is incorrect.\n\n");

        fb.append("Expected Output:\n");
        fb.append(normExpected).append("\n\n");

        fb.append("Your Output:\n");
        fb.append(normActual).append("\n\n");

        // Additional guided insights
        if (normActual.contains("Exception")) {
            fb.append("It looks like your code threw an exception. Check array indices, null values, etc.\n");
        }

        if (normExpected.length() != normActual.length()) {
            fb.append("Output lengths differ. You may have extra/missing content.\n");
        }

        fb.append("\nHint: Ensure your logic handles all test cases correctly.");
        return fb.toString();
    }
}
