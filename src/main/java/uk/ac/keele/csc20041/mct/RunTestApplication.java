package uk.ac.keele.csc20041.mct;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Take part in a test
 *
 * Called through
 * ```
 * mct test 'test_name'
 * ```
 *
 * @author Samual
 */
public class RunTestApplication {
    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
            return;
        }
        TestKlass test;
        try {
            test = new TestKlass(args[0]);
        } catch (FileNotFoundException ex) {
            System.err.println("Cannot find Test: " + args[0]);
            System.exit(1);
            return;
        } catch (IOException ex) {
            System.err.println("System error!");
            System.exit(1);
            return;
        }
        Attempt att = test.createAttempt(System.out);
        
        att.run();
        
        try {
            att.save();
        } catch (IOException ex) {
            System.err.println("Error saving attempt");
            System.exit(1);
        }
    }

    public static void printHelp() {
        System.out.println("Run help...");
    }
}
