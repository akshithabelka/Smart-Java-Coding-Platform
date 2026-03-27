import java.util.*;

public class Main {
    public static void main(String[] args) {
         Scanner scanner = new Scanner(System.in);
        int num1 = scanner.nextInt(); // Reads the first integer
        int num2 = scanner.nextInt(); // Reads the second integer
        scanner.close(); // Close the scanner to prevent resource leaks

        int sum = num1 + num2;
        System.out.println(sum);
    }
}
  
