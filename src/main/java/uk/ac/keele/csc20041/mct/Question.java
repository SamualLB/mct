package uk.ac.keele.csc20041.mct;

import java.io.IOException;
import java.io.Writer;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

/**
 * A question which is given to the user to answer
 * 
 * Stores the test of the question and each of the 4 answers
 * 
 * @author Samual
 */
public class Question implements JSONStreamAware {
    private final String text;
    private final String answerA;
    private final String answerB;
    private final String answerC;
    private final String answerD;
    private final char correctAnswer;
    private Character selectedAnswer;
    
    //Gets the details of a question
    protected Question(JSONObject json) {
        this.text = (String) json.get("question");
        this.answerA = (String) json.get("a");
        this.answerB = (String) json.get("b");
        this.answerC = (String) json.get("c");
        this.answerD = (String) json.get("d");
        this.correctAnswer = ((String) json.get("answer")).charAt(0);
        this.selectedAnswer = null;
    }

    public Question(
            String ques,
            String ansA,
            String ansB,
            String ansC,
            String ansD,
            char ansCorrect) {
        this.text = ques;
        this.answerA = ansA;
        this.answerB = ansB;
        this.answerC = ansC;
        this.answerD = ansD;
        this.correctAnswer = ansCorrect;
        this.selectedAnswer = null;
    }

    //Question with an answer that has been selected
    public Question(
            String ques,
            String ansA,
            String ansB,
            String ansC,
            String ansD,
            char ansCorrect,
            Character ansSelection) {
        this.text = ques;
        this.answerA = ansA;
        this.answerB = ansB;
        this.answerC = ansC;
        this.answerD = ansD;
        this.correctAnswer = ansCorrect;
        this.selectedAnswer = ansSelection;
    }

    public boolean correct() {
        if (this.selectedAnswer == null)
            return false;
        return this.correctAnswer == this.selectedAnswer;
    }

    //Get method for retrieving the question text
    public String getQuestion() {
        return this.text;
    }

    //Get methods for retrieving each answer (A, B, C or D)
    public String getAnswerA() {
        return this.answerA;
    }

    public String getAnswerB() {
        return this.answerB;
    }

    public String getAnswerC() {
        return this.answerC;
    }

    public String getAnswerD() {
        return this.answerD;
    }
    
    //Get methods for retrieving the correct and selected answers)
    public char getCorrectAnswer() {
        return this.correctAnswer;
    }
    
    public Character getSelectedAnswer() {
        return this.selectedAnswer;
    }
    
    /**
     * Change or set the selected answer
     * 
     * @param newAnswer 
     */
    public void setAnswer(Character newAnswer) {
        // If null don't check for ABCD and just set
        if (newAnswer == null) {
            this.selectedAnswer = null;
            return;
        }

        // Convert lowercase abcd to uppercase ABCD
        if ("abcd".indexOf(newAnswer) >= 0) 
            newAnswer = (char) (newAnswer - 32);

        // If not ABC or D, throw
        if ("ABCD".indexOf(newAnswer) < 0)
            throw new IllegalArgumentException("Invalid answer (" + newAnswer + ')');

        this.selectedAnswer = newAnswer;
    }


    @Override
    public void writeJSONString(Writer out) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("question", this.text);
        obj.put("a", this.answerA);
        obj.put("b", this.answerB);
        obj.put("c", this.answerC);
        obj.put("d", this.answerD);
        obj.put("answer", "" + this.correctAnswer);
        JSONValue.writeJSONString(obj, out);
    }
}
