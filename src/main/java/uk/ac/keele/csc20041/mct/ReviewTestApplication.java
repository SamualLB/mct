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
        String[][] results = new String[test.noAttempts()][headers.length];
        int i = 0;
        for (Attempt att : test.getAttempts()) {
            String[] arr = new String[headers.length];
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
        TestKlass test;
        Attempt att;
        try {
            test = new TestKlass(args[0]);
            att = test.getAttempt(args[2]);
        } catch (FileNotFoundException ex) {
            System.err.println("Test " + args[0] + " cannot be found");
            return;
        } catch (IOException ex) {
            System.err.println("System error.");
            return;
        }
        System.out.println("Results for student " + args[2] + " taking test " + test.getName());
        System.out.println(SEPARATOR);
        // Question | Correct | Given | Actual
        //
        // Q1 | NO | C | B
        String[] headers = {"Question", "Correct", "Given", "Actual"};
        String[][] results = new String[att.noQuestions()][headers.length];
        int i = 0;
        for (Question q : att.getQuestions()) {
            String[] arr = new String[headers.length];
            arr[0] = "Q" + (i + 1);
            arr[1] = q.correctText();
            Character ans = q.getSelectedAnswer();
            arr[2] = (ans == null) ? "-" : ans.toString();
            arr[3] = "" + q.getCorrectAnswer();
            results[i] = arr;
            i++;
        }
        table(headers, results);
        System.out.println(SEPARATOR);
    }

    /**
     * Summary of score for each question
     *
     * '`test_name` question'
     */
    public static void questions(String[] args) {
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
        System.out.println("Result summary for " + test.getName());
        System.out.println(SEPARATOR);
        // Question | Mark
        //
        // Q1 | 1/2
        // Q2 | 2/2
        String[] headers = {"Question", "Mark"};
        String[][] results = new String[test.noQuestions()][headers.length];
        int i = 0;
        for (Question q : test.getQuestions()) {
            String[] arr = new String[headers.length];
            arr[0] = "Q" + (i + 1);
            int noCorrect = 0;
            for (int n = 0; n < test.noAttempts(); n++) {
                if (test.getAttempts().get(n).getQuestions().get(i).correct())
                    noCorrect++;
            }
            arr[1] = noCorrect + "/" + test.noAttempts();
            results[i] = arr;
            i++;
        }
        table(headers, results);
        System.out.println(SEPARATOR);
    }
    
    /**
     * Summary of how a specific question was answered
     *
     * '`test_name` question `question_no`'
     */
    public static void question(String[] args) {
        TestKlass test;
        Integer qNo;
        int qIndex;
        try {
            test = new TestKlass(args[0]);
            qNo = Integer.parseInt(args[2]);
            qIndex = qNo - 1;
        } catch (FileNotFoundException ex) {
            System.err.println("Test " + args[0] + " cannot be found");
            return;
        } catch (IOException ex) {
            System.err.println("System error.");
            return;
        }
        int correctAttempts = 0;
        int aCount = 0;
        int bCount = 0;
        int cCount = 0;
        int dCount = 0;
        int nullCount = 0;
        for (Attempt att : test.getAttempts()) {
            if (att.getQuestions().get(qIndex).correct())
                correctAttempts++;
            Character nullable = att.getQuestions().get(qIndex).getSelectedAnswer();
            if (nullable == null) {
                nullCount++;
                continue;
            }
            switch (nullable) {
                case 'A':
                    aCount++;
                    break;
                case 'B':
                    bCount++;
                    break;
                case 'C':
                    cCount++;
                    break;
                case 'D':
                    dCount++;
                    break;
            }
        }
        System.out.println(SEPARATOR);
        System.out.println("Results for " + test.getName());
        System.out.println("Question " + qNo + "/" + test.noQuestions());
        System.out.println("Mark " + correctAttempts + "/" + test.noAttempts());
        System.out.println();
        System.out.println(test.getQuestions().get(qIndex).getQuestion());
        System.out.println();
        System.out.println("A: " + test.getQuestions().get(qIndex).getAnswerA() + " (" + aCount + ")");
        System.out.println("B: " + test.getQuestions().get(qIndex).getAnswerB() + " (" + bCount + ")");
        System.out.println("C: " + test.getQuestions().get(qIndex).getAnswerC() + " (" + cCount + ")");
        System.out.println("D: " + test.getQuestions().get(qIndex).getAnswerD() + " (" + dCount + ")");
        System.out.println("   No answer given. (" + nullCount + ")");
        System.out.println();

        String[] headers = {"Student ID", "Choice", "Correct"};
        String[][] results = new String[test.noAttempts()][headers.length];
        int i = 0;
        for (Attempt att : test.getAttempts()) {
            String[] arr = new String[headers.length];
            arr[0] = att.getStudentId();
            Character choice = att.getQuestions().get(qIndex).getSelectedAnswer();
            arr[1] = (choice == null) ? "none" : choice.toString();
            arr[2] = att.getQuestions().get(qIndex).correctText();
            results[i] = arr;
            i++;
        }
        table(headers, results);
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
