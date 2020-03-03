package uk.ac.keele.csc20041.mct;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Application {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("Multiple Choice Test Application!");
        
        
        //Test comment 1
        
        // Test CSV Parsing
        Reader in = new FileReader("questions.csv");
        int qs = 0;
        for (CSVRecord record : CSVFormat.DEFAULT.withHeader("question", "a", "b", "c", "d", "answer").parse(in)) {
            System.out.println("Q" + (qs+1) + ": " + record.get("question"));
            System.out.println();
            System.out.println("A: " + record.get("a"));
            System.out.println("B: " + record.get("b"));
            System.out.println("C: " + record.get("c"));
            System.out.println("D: " + record.get("d"));
            System.out.println();
            System.out.println("Answer: " + record.get("answer"));
            System.out.println("==========");
            qs++;
        }
    }
}
