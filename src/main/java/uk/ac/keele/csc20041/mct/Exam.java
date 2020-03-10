package uk.ac.keele.csc20041.mct;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * A collection of all the questions.
 *
 * Can be used to generate a Test.
 *
 * @author Samual
 */
public class Exam {
    public static final String FILE_EXTENSION = ".mcte"; // MCT Exam

    private final ArrayList<Question> questions;
    private final int timeLimit;
    private final int noQuestions;

    /**
     * Looks for the given file, or with the addition of the 'json' extension
     *
     * @param path Path of the file name
     * @return Returns a FileReader over the file found
     * @throws FileNotFoundException When the file does not exist
     */
    private static FileReader resolveFileName(String path) throws FileNotFoundException {
        try {
            return new FileReader(path);
        } catch (FileNotFoundException e) {
            return new FileReader(path + FILE_EXTENSION);
        }
    }

    /**
     * Generate from a file name
     *
     * This can be called straight from the arguments, it looks for the file
     * with the file name with or without the 'json' extension
     *
     * @param fileName Name of the file to parse
     * @throws FileNotFoundException When the file does not exist
     * @throws IOException Error reading the file
     */
    Exam(String fileName) throws FileNotFoundException, IOException {
        JSONObject json = (JSONObject) JSONValue.parse(resolveFileName(fileName));

        this.timeLimit = (int) (long) json.get("time_limit");
        this.noQuestions = (int) (long) json.get("no_questions");

        this.questions = new ArrayList();
        for (Object val : (JSONArray) json.get("questions")) {
            this.questions.add(new Question((JSONObject) val));
        }
    }
    
    Exam(ArrayList<Question> questions, int timeLimit, int noQuestions) {
        this.questions = questions;
        this.timeLimit = timeLimit;
        this.noQuestions = noQuestions;
    }

    /**
     * All of the questions in the exam
     *
     * @return ArrayList of Question for the pool of questions
     */
    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    /**
     * @return The number of seconds for the test
     */
    public int getTimeLimit() {
        return this.timeLimit;
    }

    /**
     * Not the number of questions in here, only the number that should be
     * generated 
     *
     * @return The number of questions that should be in the test
     */
    public int getNoOfQuestions() {
        return this.noQuestions;
    }
    
    /**
     * Generate a randomised Test
     * 
     * @param name Name of the test
     * @return TestKlass with the same properties and a randomised subset of questions
     */
    public TestKlass generateTest(String name) {
        return new TestKlass(name, generateSample(), this.timeLimit);
    }

    /**
     * Generate a randomised subset of the questions
     * 
     * Adapted from https://www.javamex.com/tutorials/random_numbers/random_sample.shtml
     *
     * @return The ArrayList of the correct size
     */
    private ArrayList<Question> generateSample(Random r) {
        ArrayList<Question> newList = new ArrayList(this.noQuestions);
        int nPicked = 0, i = 0, nLeft = this.noQuestions;
        int nSamplesNeeded = this.noQuestions;
        while (nSamplesNeeded > 0) {
            int rand = r.nextInt(nLeft);
            if (rand < nSamplesNeeded) {
                newList.add(nPicked++, this.questions.get(i));
                nSamplesNeeded--;
            }
            nLeft--;
            i++;
        }
        return newList;
    }
    
    /**
     * Unseeded random
     * 
     * @return ArrayList of correct size
     */
    private ArrayList<Question> generateSample() {
        return generateSample(new Random());
    }
}
