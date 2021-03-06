package uk.ac.keele.csc20041.mct;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

/**
 * @author Samual
 */
public class TestKlass implements JSONStreamAware {
    private final String name;
    private final ArrayList<Question> questions;
    private final int timeLimit;
    private final String passcode;
    private final ArrayList<Attempt> attempts;
    
    private static final String PASSCODE_STRING = "0123456789";
    private static final int PASSCODE_LENGTH = 6;
    public static final String FILE_EXTENSION = ".mctt"; // MCT Test

    /**
     * Create a new Test with the set properties
     *
     * @param name Name to save the test with
     * @param questions Questions to ask on the test
     * @param timeLimit Time limit for the test
     */
    public TestKlass(String name, ArrayList<Question> questions, int timeLimit) {
        this.name = name;
        this.questions = questions;
        this.attempts = new ArrayList<>();
        this.timeLimit = timeLimit;
        this.passcode = generatePasscode();
    }

    /**
     * Create from an existing file
     *
     * @param fileName Path of the file
     * @throws FileNotFoundException File does not exist
     * @throws IOException IO Error
     */
    public TestKlass(String fileName) throws FileNotFoundException, IOException {
        File file = new File(fileName);

        // If the file does exist
        if (file.exists()) {
            // Remove the extension for the name
            this.name = fileName.substring(0, fileName.lastIndexOf('.'));
        } else {
            // If the user enters a filename without the .mctt extension,
            // the file does not exist so this path is taken
            file = new File(fileName + FILE_EXTENSION);
            this.name = fileName;
            if (!file.exists())
                // If the file still does not exist, an exceeption is thrown as
                // the user may have entered the wrong name
                throw new FileNotFoundException("Cannot find test file: " + fileName);
        }

        JSONObject json = (JSONObject) JSONValue.parse(new FileReader(file));

        //Gets the time limit and passcode
        this.timeLimit = (int) (long) json.get("time_limit");
        this.passcode = (String) json.get("passcode");

        this.questions = new ArrayList<>();
        JSONArray qJSONArray = (JSONArray) json.get("questions");
        for (Object qJSON : qJSONArray) {
            this.questions.add(new Question((JSONObject) qJSON));
        }

        this.attempts = new ArrayList<>();
        JSONObject aJSONHash = (JSONObject) json.get("attempts");
        Iterator iter = aJSONHash.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry ele = (Map.Entry) iter.next();
            this.attempts.add(new Attempt(this, (JSONArray) ele.getValue(), (String) ele.getKey()));
        }
    }

    /**
     * Name of this test
     *
     * @return Name as String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Shallow clone of the questions
     *
     * @return ArrayList of questions
     */
    public ArrayList<Question> getQuestions() {
        return this.questions;
    }
    
    /**
     * Number of questions in this test
     *
     * @return Number of questions
     */
    public int noQuestions() {
        return this.questions.size();
    }
    
    /**
     * Find attempt by student id
     *
     * @param id Student id
     * @return The Attempt or null
     */
    public Attempt getAttempt(String id) {
        for (Attempt a : this.attempts) {
            if (a.getStudentId().equals(id))
                return a;
        }
        return null;
    }
    
    public ArrayList<Attempt> getAttempts() {
        return this.attempts;
    }
    
    public int noAttempts() {
        return this.attempts.size();
    }
    
    /**
     * Make a clone of the questions
     *
     * This allows each Attempt to have its own modifiable questions
     *
     * @return Cloned questions
     * @throws CloneNotSupportedException Should not happen
     */
    public ArrayList<Question> cloneQuestions() throws CloneNotSupportedException {
        ArrayList<Question> clone = new ArrayList<>(this.questions.size());
        for (Question i : this.questions)
            clone.add((Question) i.clone());
        return clone;
    }

    /**
     * Time limit of this test in seconds
     *
     * @return Number of seconds
     */
    public int getTimeLimit() {
        return this.timeLimit;
    }

    /**
     * Get the correct passcode for this test
     *
     * @return Passcode as a String
     */
    protected String getPasscode() {
        return this.passcode;
    }

    private String generatePasscode() {
        Random r = new Random();
        StringBuilder b = new StringBuilder(PASSCODE_LENGTH);
        for (int i = 0; i < PASSCODE_LENGTH; i++) {
            b.append(PASSCODE_STRING.charAt(r.nextInt(PASSCODE_STRING.length())));
        }
        return b.toString();
    }

    /**
     * Save the test to the file system so that it can be run
     *
     * @return If it was saved successfully
     * @throws java.io.IOException Error writing to file
     */
    public boolean saveToFile() throws IOException {
        File file = new File(this.name + FILE_EXTENSION);
        if (!file.createNewFile())
            return false;
        FileWriter writer = new FileWriter(file);
        writeJSONString(writer);
        writer.close();
        return true;
    }
    
    /**
     * Save an attempt to this test's file
     *
     * @param att Attempt to add
     * @return If save was successful
     * @throws java.io.IOException
     */
    public boolean saveAttempt(Attempt att) throws IOException {
        this.attempts.add(att);
        File file = new File(this.name + FILE_EXTENSION);
        FileWriter writer = new FileWriter(file);
        writeJSONString(writer);
        writer.close();
        return true;
    }

    /**
     * Write this Test as JSON
     *
     * Includes individual properties including questions and the attempts
     *
     * Questions are a simple JSON Array
     *
     * Attempts are an object with the students' ids as keys and an array
     * of single-letter string answers
     *
     * @param out Writer
     * @throws IOException On IO Error
     */
    @Override
    public void writeJSONString(Writer out) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("time_limit", this.timeLimit);
        obj.put("passcode", this.passcode);
        JSONArray questionArr = new JSONArray();
        questionArr.addAll(this.questions);
        obj.put("questions", questionArr);
        JSONObject attemptsObj = new JSONObject();
        for (Attempt attempt : this.attempts) {
            attemptsObj.put(attempt.getStudentId(), attempt);
        }
        obj.put("attempts", attemptsObj);
        JSONValue.writeJSONString(obj, out);
    }
    
    public Attempt createAttempt(PrintStream out) {
        return new Attempt(this, out);
    }
}
