package uk.ac.keele.csc20041.mct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

/**
 * An attempt of a specific test
 *
 * @author Samual
 */
public class Attempt implements JSONStreamAware {
    private final TestKlass test;
    private final int[] questionMap;
    private String studentId = null;
    private final ArrayList<Question> questions;
    private final BufferedReader in;
    private final PrintStream out;
    
    private long startTime = 0;
    private int currentQuestion = 0;
    
    private static final int COLS = 72;
    private static final String SEPARATOR = "========================================================================";
    private static final String INPUT_START = "> ";
    
    public Attempt(TestKlass test, PrintStream output) {
        this.test = test;
        this.out = output;
        ArrayList<Question> qs;
        try {
            qs = test.cloneQuestions();
        } catch (CloneNotSupportedException ex) {
            System.err.println("ERROR CLONING QUESTIONS: " + ex.getLocalizedMessage());
            qs = new ArrayList<>();
        }
        this.questions = qs;
        this.questionMap = new int[getQuestions().size()];
        for (int i = 0; i < this.questionMap.length; i++) {
            this.questionMap[i] = i;
        }
        shuffle(this.questionMap);
        this.in = new BufferedReader(new InputStreamReader(System.in));
    }
    
    protected Attempt(TestKlass test, JSONArray json, String id) {
        this.test = test;
        this.out = null;
        ArrayList<Question> qs;
        try {
            qs = test.cloneQuestions();
        } catch (CloneNotSupportedException ex) {
            System.err.println("ERROR CLONING QUESTIONS: " + ex.getLocalizedMessage());
            qs = new ArrayList<>();
        }
        this.questions = qs;
        int i = 0;
        for (Question q : this.questions) {
            String strVal = (String) json.get(i);
            q.setAnswer(strVal == null ? null : strVal.charAt(0));
            i++;
        }
        this.questionMap = new int[0];
        this.in = null;
        this.studentId = id;
    }
    
    public final ArrayList<Question> getQuestions() {
        return this.questions;
    }
    
    public final int getTimeLimit() {
        return this.test.getTimeLimit();
    }
    
    public final String getStudentId() {
        return this.studentId;
    }
    
    /**
     * Has an answer been entered for all questions
     * 
     * @return True if all answered, false if any are not
     */
    public final boolean allAnswered() {
        for (Question q : getQuestions()) {
            if (q.getSelectedAnswer() == null)
                return false;
        }
        return true;
    }
    
    /**
     * The number of question in the test
     * 
     * @return The number of question in the test
     */
    public final int noQuestions() {
        return this.questionMap.length;
    }
    
    /**
     * Get the corresponding, mapped question
     * 
     * This is used during the test as the method for randomising the order
     * seen by the user
     *
     * @param i Question number
     * @return A Question
     */
    public final Question getQuestion(int i) {
        return this.getQuestions().get(questionMap[i]);
    }
    
    private ArrayList<Integer> getUnanswered() {
        ArrayList<Integer> arr = new ArrayList(noQuestions());
        for (int i = 0; i < noQuestions(); i++) {
            if (getQuestion(i).getSelectedAnswer() == null)
                arr.add(i);
        }
        return arr;
    }
    
    /**
     * Wordy amount of time in minutes and seconds
     *
     * @param seconds Time in seconds
     * @return String value of number of seconds
     */
    public final String humanTime(int seconds) {
        if (seconds < 60)
            return seconds + " seconds";
        else if (seconds % 60 == 0)
            return seconds / 60 + " minutes";
        else
            return seconds / 60 + " minutes " + seconds % 60 + " seconds";
    }
    
    /**
     * String for the timer now
     *
     * @return The timer String
     */
    public final String timer() {
        return timer(System.nanoTime());
    }
    
    /**
     * The timer String at a time
     *
     * @param timeNow Time in nanoseconds
     * @return The timer String "1:30"
     */
    public final String timer(long timeNow) {
        long timeTaken = timeNow - this.startTime;
        long nanoseconds = ((long) this.getTimeLimit() * 1_000_000_000) - timeTaken;
        long seconds = (long) Math.ceil(nanoseconds / 1_000_000_000.0);
        String nSeconds = "" + (seconds % 60);
        if (nSeconds.length() == 1) {
            return (seconds / 60) + ":0" + nSeconds;
        }
        return (seconds / 60) + ":" + nSeconds;
    }
    
    /**
     * If the time is up according to the current time
     *
     * @return If time is up
     */
    public boolean timeUp() {
        return timeUp(System.nanoTime());
    }
    
    /**
     * Is time up, according to the given time
     *
     * @param timeNow Time in nanoseconds
     * @return If the time is up
     */
    public boolean timeUp(long timeNow) {
        long timeTaken = timeNow - this.startTime;
        long nanoSeconds = ((long) this.getTimeLimit() * 1_000_000_000) - timeTaken;
        return nanoSeconds <= 0;
    }
    
    public void run() {
        // Enter student number
        readStudentNumber();
        // Enter passcode
        readPasscode();
        // Enter 'ready' to begin
        readReady();
        
        // Start actual test
        startTime = System.nanoTime();
        boolean exitedEarly = false;
        currentQuestion = 0;
        String inp;
        
        // Loop until user want to exit or time runs out
        while (!exitedEarly && !timeUp()) {
            // Draw test name, timer
            drawTestInfo();
            if (currentQuestion < noQuestions()) {
                // Draw the current question's info
                drawQuestion(currentQuestion);
            } else {
                // Last question was answered, display review screen
                drawReview();
                out.println();
                out.print(INPUT_START);
            }
            inp = readLine(); // Blocking!
            // If the time is expired, stop the test
            if (timeUp()) {
                break;
            }
            switch (inp) {
                // If an answer's letter is entered
                case "A":
                case "B":
                case "C":
                case "D":
                case "a":
                case "b":
                case "c":
                case "d":
                    // Set the answer and go to the next question
                    getQuestion(currentQuestion).setAnswer(inp.charAt(0));
                    out.println(
                            "You answered " +
                            getQuestion(currentQuestion).getSelectedAnswer() +
                            " to Question " +
                            (currentQuestion + 1) +
                            ".");
                    currentQuestion++;
                    break;
                // If exit is entered
                case "exit":
                    int nextQ = exitScreen();
                    if (nextQ < 0) {
                        exitedEarly = true;
                    } else {
                        currentQuestion = nextQ;
                    }
                    break;
                default:
                    try {
                        // If a question number is ented, go to that question
                        int newQNo = Integer.parseInt(inp);
                        if (newQNo > 0 && newQNo <= noQuestions())
                            currentQuestion = newQNo - 1;
                    } catch (NumberFormatException ex) {
                    }
            }
        }
        if (!exitedEarly)
            out.println("Time elapsed.");
        drawTestOver();
    }
    
    /**
     * Add this attempt to the test, then save the test
     *
     * @return
     * @throws IOException
     */
    public boolean save() throws IOException {
        return this.test.saveAttempt(this);
    }
    
    private int exitScreen() {
        drawTestInfo();
        drawReview();
        String inp;
        while (true) {
            out.print(INPUT_START);
            inp = readLine();
            if (inp.equals("exit") || timeUp())
                return -1;
            try {
                int newQNo = Integer.parseInt(inp);
                if (newQNo > 0 && newQNo <= noQuestions())
                    return newQNo - 1;
            } catch (NumberFormatException ex) {
            }
        }
    }
    
    private void drawTestInfo() {
        out.println(SEPARATOR);
        out.print(this.test.getName());
        String timer = timer();
        out.print(" ".repeat(COLS - timer.length() - this.test.getName().length()));
        out.println(timer);
    }
    
    private void drawQuestion(int n) {
        String qStatus = "Q " + (n+1) + "/" + noQuestions();
        out.print(" ".repeat(COLS - qStatus.length()));
        out.println(qStatus);
        out.println();
        
        Question q = getQuestion(n);
        
        out.println("Question " + (n+1) + ": " + q.getQuestion());
        out.println();
        
        if (q.getSelectedAnswer() != null) {
            out.println("Your answer: " + q.getSelectedAnswer());
            out.println();
        }
        
        out.println("A: " + q.getAnswerA());
        out.println("B: " + q.getAnswerB());
        out.println("C: " + q.getAnswerC());
        out.println("D: " + q.getAnswerD());
        out.print(INPUT_START);
    }
    
    private void drawReview() {
        if (allAnswered()) {
            out.println();
            out.println("You have answered all the questions.");
            out.println();
            out.println("You can navigate to a question to review, or enter 'exit' to end the test early.");
        } else {
            ArrayList<Integer> unanswered = getUnanswered();
            out.println("There are still " + unanswered.size() + " questions:");
            out.print((unanswered.get(0) + 1));
            if (unanswered.size() == 1) {
                out.println();
            } else {
                for (int i = 1; i < unanswered.size() - 1; i++) {
                    out.print(", " + (unanswered.get(i) + 1));
                }
                out.println(" & " + (unanswered.get(unanswered.size()-1) + 1));
            }
        }
    }
    
    private void drawTestOver() {
        out.println(SEPARATOR);
        out.println("Test " + this.test.getName() + " complete.");
        out.println("Your results have been saved.");
        out.println(SEPARATOR);
    }
    
    /**
     * Loop until a valid student ID is input
     */
    public void readStudentNumber() {
        out.println(SEPARATOR);
        out.println("Starting test " + this.test.getName());
        while (this.studentId == null) {
            out.println("Please enter your student number:");
            out.print(INPUT_START);
            String inp = readLine();
            if (validStudentId(inp)) {
                this.studentId = inp;
                return;
            }
            out.println(SEPARATOR);
            out.println("Invalid Student ID");
        }
    }
    
    public void readPasscode() {
        out.println(SEPARATOR);
        out.println("Starting test " + this.test.getName());
        while (true) {
            out.println("Please enter the passcode to begin the test:");
            out.print(INPUT_START);
            String inp = readLine();
            if (inp.equals(this.test.getPasscode()))
                return;
            out.println(SEPARATOR);
            out.println("Incorrect test passcode");
        }
    }
    
    public void readReady() {
        out.println(SEPARATOR);
        out.println("Test " + this.test.getName() + " ready to begin.");
        out.println(
                "This test contains " +
                noQuestions() +
                " questions and has a time limit of " +
                humanTime(this.test.getTimeLimit()) +
                ".");
        while (true) {
            out.println("Enter 'ready' to begin the test.");
            out.print(INPUT_START);
            String inp = readLine();
            if (inp.equals("ready"))
                return;
        }
    }
    
    private static boolean validStudentId(String test) {
        return test.matches("\\d{8}");
    }
    
    public String readLine() {
        try {
            return in.readLine();
        } catch (IOException ex) {
            return "";
        }
    }
    
    private static void shuffle(int[] arr) {
        Random r = new Random();
        for (int i = arr.length - 1; i > 0; i--) {
            shuffleSwap(arr, i, r.nextInt(i + 1));
        }
    }

    private static void shuffleSwap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    @Override
    public void writeJSONString(Writer out) throws IOException {
        JSONArray arr = new JSONArray();
        for (Question q : this.getQuestions())
            arr.add(q.getSelectedAnswer() == null ? null : q.getSelectedAnswer().toString());
        JSONValue.writeJSONString(arr, out);
    }
}
