package uk.ac.keele.csc20041.mct;

import java.io.File;
import java.io.FileWriter;
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

    public String getName() {
        return this.name;
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    public int getTimeLimit() {
        return this.timeLimit;
    }

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
     */
    public boolean saveToFile() {
        try {
            File file = new File(this.name + ".json");
            if (!file.createNewFile())
                return false;
            FileWriter writer = new FileWriter(file);
            writeJSONString(writer);
            System.out.println("Wrote to file " + file.toString());
            writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
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
