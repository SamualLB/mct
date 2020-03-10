package uk.ac.keele.csc20041.mct;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

/**
 * @author Samual
 */
public class Test implements JSONStreamAware {
    private final String name;
    private final ArrayList<Question> questions;
    private final int timeLimit;
    private final String passcode;
    
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
    public Test(String name, ArrayList<Question> questions, int timeLimit) {
        this.name = name;
        this.questions = questions;
        this.timeLimit = timeLimit;
        this.passcode = generatePasscode();
    }

    Test(String fileName) throws FileNotFoundException, IOException {
        File file = new File(fileName);
        
        // If the file does exist
        if (file.exists()) {
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
    }

    //Get method to retrieve name
    public String getName() {
        return this.name;
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    //Get method to retrieve timelimit
    public int getTimeLimit() {
        return this.timeLimit;
    }

    //Get method to retrieve passcode
    protected String getPasscode() {
        return this.passcode;
    }

    //Generates the passcode
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

    @Override
    public void writeJSONString(Writer out) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("time_limit", this.timeLimit);
        obj.put("passcode", this.passcode);
        JSONArray questionArr = new JSONArray();
        questionArr.addAll(this.questions);
        obj.put("questions", questionArr);
        JSONValue.writeJSONString(obj, out);
    }
    
    
}
