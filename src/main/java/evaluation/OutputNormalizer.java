package evaluation;

public class OutputNormalizer {

    public static String normalize(String output) {
        if (output == null) return "";

        // Remove leading/trailing spaces
        output = output.trim();

        // Replace multiple spaces & tabs with a single space
        output = output.replaceAll("[ \\t]+", " ");

        // Normalize newline inconsistencies
        output = output.replaceAll("\\r\\n", "\n"); // Windows → Unix
        output = output.replaceAll("\\r", "\n");

        // Remove blank lines at the end
        output = output.replaceAll("\\n+", "\n").trim();

        return output;
    }
}
