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
            //Creates new exam and test objects
            Exam exam = new Exam(args[0]);
            TestKlass test = exam.generateTest(args[1]);
            
            //If the file is already present
            if (!test.saveToFile()) {
                System.err.println("Test already exists, cannot overwrite");
                return;
            }
            
            System.out.println("========================================================================");
            System.out.println("Created test: " + test.getName());
            System.out.println("The passcode for the test is: " + test.getPasscode());
            System.out.println("========================================================================");
            
            //Catching exceptions
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
