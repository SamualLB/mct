package uk.ac.keele.csc20041.mct;

import java.util.ArrayList;

/**
 * @author Samual
 */
public class Test {
    private final String name;
    private final ArrayList<Question> questions;
    private final int timeLimit;
    
    public Test(String name, ArrayList<Question> questions, int timeLimit) {
        this.name = name;
        this.questions = questions;
        this.timeLimit = timeLimit;
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
}
