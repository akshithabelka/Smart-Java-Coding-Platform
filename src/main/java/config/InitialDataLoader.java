package config;

import models.Difficulty;
import services.ProblemService;

public class InitialDataLoader {

    public static void loadDefaults() {

        ProblemService ps = new ProblemService();

        try {
            /*
             * PROBLEM 1: SUM OF TWO NUMBERS
             */
            var p1 = ps.createProblem(
                    "Sum of Two Numbers",
                    "Given two integers, print their sum.\n" +
                            "Input: two integers separated by space\n" +
                            "Output: a single integer (the sum)",
                    Difficulty.EASY
            );
            ps.addTestCase(p1.getProblemId(), "2 3", "5", false);
            ps.addTestCase(p1.getProblemId(), "10 20", "30", false);
            ps.addTestCase(p1.getProblemId(), "100 250", "350", true);


            /*
             * PROBLEM 2: REVERSE STRING
             */
            var p2 = ps.createProblem(
                    "Reverse a String",
                    "Given a string, print its reverse.\n" +
                            "Input: a single string\n" +
                            "Output: reversed string",
                    Difficulty.MEDIUM
            );
            ps.addTestCase(p2.getProblemId(), "hello", "olleh", false);
            ps.addTestCase(p2.getProblemId(), "java", "avaj", false);
            ps.addTestCase(p2.getProblemId(), "programming", "gnimmargorp", true);


            /*
             * PROBLEM 3: CHECK PRIME
             */
            var p3 = ps.createProblem(
                    "Check Prime Number",
                    "Determine whether a number is prime.\n" +
                            "Input: a single integer n\n" +
                            "Output: 'prime' or 'not prime'",
                    Difficulty.MEDIUM
            );
            ps.addTestCase(p3.getProblemId(), "5", "prime", false);
            ps.addTestCase(p3.getProblemId(), "12", "not prime", false);
            ps.addTestCase(p3.getProblemId(), "97", "prime", true);


            /*
             * PROBLEM 4: FACTORIAL
             */
            var p4 = ps.createProblem(
                    "Factorial of a Number",
                    "Compute factorial of n.\n" +
                            "Input: integer n (0 ≤ n ≤ 12)\n" +
                            "Output: n! (factorial of n)",
                    Difficulty.MEDIUM
            );
            ps.addTestCase(p4.getProblemId(), "5", "120", false);
            ps.addTestCase(p4.getProblemId(), "0", "1", false);
            ps.addTestCase(p4.getProblemId(), "7", "5040", true);


            /*
             * PROBLEM 5: Nth Fibonacci
             */
            var p5 = ps.createProblem(
                    "Nth Fibonacci Number",
                    "Given an integer n, print the nth Fibonacci number.\n" +
                            "F(0)=0, F(1)=1\n" +
                            "Input: n\n" +
                            "Output: nth Fibonacci number",
                    Difficulty.HARD
            );
            ps.addTestCase(p5.getProblemId(), "5", "5", false);
            ps.addTestCase(p5.getProblemId(), "10", "55", false);
            ps.addTestCase(p5.getProblemId(), "12", "144", true);

            System.out.println("Default problems loaded successfully!");

        } catch (Exception e) {
            System.out.println("Error adding default problems: " + e.getMessage());
        }
    }
}
