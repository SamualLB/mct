package uk.ac.keele.csc20041.mct;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * A collection of all the questions.
 *
 * Can be used to generate a Test.
 *
 * TODO: time limit and number of questions for generated test
 *
 * @author Samual
 */
public class Exam {
    private final ArrayList<Question> questions;
    //private final int timeLimit;
    //private final int noQuestions;

    private static FileReader resolveFileName(String path) throws FileNotFoundException {
        try {
            return new FileReader(path);
        } catch (FileNotFoundException e) {
            return new FileReader(path + ".csv");
        }
    }

    /**
     * Build an Exam from a filename
     *
     * @param fileName The CSV file with the questions
     * @throws FileNotFoundException Exam file does not exist
     * @throws IOException Error reading the file
     */
    Exam(String fileName) throws FileNotFoundException, IOException {
        FileReader reader = resolveFileName(fileName);

        CSVParser parser = CSVFormat.DEFAULT
                .withHeader("question", "a", "b", "c", "d", "answer")
                .parse(reader);

        questions = new ArrayList();
        for (CSVRecord record : parser) {
            questions.add(new Question(record));
        }
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }
}
