package uk.ac.keele.csc20041.mct;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Samual
 */
public class ReviewTestApplication {
    private static final int COLS = 72;
    private static final String SEPARATOR = "========================================================================";

    /**
     * Review controller
     *
     * Just redirects to an individual controller
     *
     * 'mct review' to get here
     *
     * Only extra arguments are passed on
     *
     * @param args Extra arguments from Application.main
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            summary(args);
        } else if (args.length > 2 && args[1].equals("student")) {
            attempt(args);
        } else if (args.length >= 2 && args[1].equals("question")) {
            if (args.length == 2)
                questions(args);
            else
                question(args);
        } else {
            printHelp();
        }
    }

    /**
     * Summary of all attempts on a test
     *
     * '`test_name`'
     */
    public static void summary(String[] args) {
        TestKlass test;
        try {
            test = new TestKlass(args[0]);
        } catch (FileNotFoundException ex) {
            System.err.println("Test " + args[0] + " cannot be found");
            return;
        } catch (IOException ex) {
            System.err.println("System error.");
            return;
        }
        System.out.println(SEPARATOR);
        System.out.println("Result summary for " + test.getName());
        // Student ID | Mark | Result
        String[] headers = { "Student ID", "Mark", "Result" };
        String[][] results = new String[test.noAttempts()][3];
        int i = 0;
        for (Attempt att : test.getAttempts()) {
            String[] arr = new String[3];
            arr[0] = att.getStudentId();
            arr[1] = att.result();
            arr[2] = att.resultName();
            results[i] = arr;
            i++;
        }
        table(headers, results);
        System.out.println(SEPARATOR);
    }

    /**
     * Summary of individual student's attempt
     *
     * '`test_name` student `student_id`'
     */
    public static void attempt(String[] args) {

        System.out.println(SEPARATOR);
        String[] headers = {"Student ID", "Mark", "Result"};
    }

    /**
     * Summary of score for each question
     *
     * '`test_name` question'
     */
    public static void questions(String[] args) {
        System.out.println(SEPARATOR);
        String[] headers = {"Question", "Mark"};
    }
    
    /**
     * Summary of how a specific question was answered
     *
     * '`test_name` question `question_no`'
     */
    public static void question(String[] args) {
        System.out.println(SEPARATOR);
    }

    private static void table(String[] headers, String[][] body) {
        if (headers.length < 1)
            return;

        int[] col_widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            col_widths[i] = headers[i].length();
            for (int j = 0; j < body.length; j++) {
                if (body[j][i].length() > col_widths[i])
                    col_widths[i] = body[j][i].length();
            }
        }

        // Add 2 spaces around each cell
        for (int i = 0; i < col_widths.length; i++)
            col_widths[i] += 2;

        // Top header border
        System.out.print('┏');
        for (int i = 0; i < headers.length; i++) {
            System.out.print("━".repeat(col_widths[i]));
            if ((i + 1) == headers.length)
                System.out.println('┓');
            else
                System.out.print('┳');
        }

        // Header text
        for (int i = 0; i < headers.length; i++) {
            System.out.print('┃' + cell(headers[i], col_widths[i]));
        }
        System.out.println('┃');

        // Bottom header border
        System.out.print('┡');
        for (int i = 0; i < headers.length; i++) {
            System.out.print("━".repeat(col_widths[i]));
            if ((i + 1) == headers.length)
                System.out.println('┩');
            else
                System.out.print('╇');
        }

        // Data text
        for (int n = 0; n < body.length; n++) {
            // for each line
            for (int i = 0; i < headers.length; i++) {
                System.out.print('│' + cell(body[n][i], col_widths[i]));
            }
            System.out.println('│');
        }
        
        // Bottom border
        System.out.print('└');
        for (int i = 0; i < headers.length; i++) {
            System.out.print("─".repeat(col_widths[i]));
            if ((i + 1) == headers.length)
                System.out.println('┘');
            else
                System.out.print('┴');
        }
    }
    
    private static String cell(String str, int w) {
        int left = (w - str.length()) / 2;
        return (" ".repeat(left) + str + " ".repeat(w - left - str.length()));
    }

    public static void printHelp() {
        System.out.println("Review help...");
    }
}
