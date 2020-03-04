package uk.ac.keele.csc20041.mct;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Called through
 * ```
 * mct create 'exam_name' 'test_name'
 * ```
 *
 * @author Samual
 */
public class CreateTestApplication {
    public static void main(String[] args) {
        if (args.length < 2) {
            printHelp();
            return;
        }
        try {
            Exam exam = new Exam(args[0]);
            Test test = exam.generateTest(args[1]);
            System.out.println("Creating test from exam...");
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot find file \"" + args[0] + "\"");
        } catch (IOException ex) {
            System.err.println("System error. " + ex.getLocalizedMessage());
        }
    }

    public static void printHelp() {
        System.out.println("Create help...");
    }
}
