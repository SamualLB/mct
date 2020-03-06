package uk.ac.keele.csc20041.mct;

import java.util.Arrays;

public class Application {
    public static void main(String[] args) {
        System.out.println("Multiple Choice Test Application!");
        if (args.length > 0) {
            //Runs the main method of a class depending on which argument is given
            switch(args[0]) {
                case "create":
                    CreateTestApplication.main(Arrays.copyOfRange(args, 1, args.length));
                    break;
                case "test":
                    RunTestApplication.main(Arrays.copyOfRange(args, 1, args.length));
                    break;
                case "review":
                    ReviewTestApplication.main(Arrays.copyOfRange(args, 1, args.length));
                    break;
                default:
                    printHelp();
            }
        } else {
            printHelp();
        }
    }

    public static void printHelp() {
        System.out.println("Help here...");
    }
}
