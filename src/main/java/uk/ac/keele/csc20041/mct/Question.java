package uk.ac.keele.csc20041.mct;

import org.apache.commons.csv.CSVRecord;

/**
 * A question which is given to the user to answer
 * 
 * Stores the test of the question and each of the 4 answers
 * 
 * @author Samual
 */
public class Question {
    private final String text;
    private final String answerA;
    private final String answerB;
    private final String answerC;
    private final String answerD;
    private final char correctAnswer;
    private Character selectedAnswer;

    /**
     * Create a Question from a CSV
     * 
     * @param r An individual CSV record, can be mapped or not
     */
    protected Question(CSVRecord r) {
        if (r.isMapped("question")
                && r.isMapped("a")
                && r.isMapped("b")
                && r.isMapped("c")
                && r.isMapped("d")
                && r.isMapped("answer")) {
            this.text = r.get("question");
            this.answerA = r.get("a");
            this.answerB = r.get("b");
            this.answerC = r.get("c");
            this.answerD = r.get("d");
            this.correctAnswer = r.get("answer").charAt(0);
        } else {
            this.text = r.get(0);
            this.answerA = r.get(1);
            this.answerB = r.get(2);
            this.answerC = r.get(3);
            this.answerD = r.get(4);
            this.correctAnswer = r.get(5).charAt(0);
        }
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

    public String getQuestion() {
        return this.text;
    }

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
}
