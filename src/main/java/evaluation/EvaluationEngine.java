package evaluation;

public class EvaluationEngine {

    public boolean compare(String expected, String actual) {
        String normExpected = OutputNormalizer.normalize(expected);
        String normActual = OutputNormalizer.normalize(actual);
        return normExpected.equals(normActual);
    }

    public String evaluate(String expected, String actual) {
        if (compare(expected, actual)) {
            return "PASS";
        }
        return "FAIL";
    }

    public String generateFeedback(String expected, String actual) {
        return FeedbackGenerator.create(expected, actual);
    }
}
