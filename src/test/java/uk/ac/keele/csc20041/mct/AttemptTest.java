package uk.ac.keele.csc20041.mct;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.junit.Test;

public class AttemptTest {
    private static JSONArray buildJSON() {
        JSONArray out = new JSONArray();
        out.add("A");
        out.add("D");
        out.add(null);
        return out;
    }
    
    private static ArrayList<Question> questions() {
        ArrayList<Question> questions = new ArrayList(3);
        questions.add(new Question("Test Question 1", "A1", "A2", "A3", "A4", 'A'));
        questions.add(new Question("Test Question 2", "A1", "A2", "A3", "A4", 'B'));
        questions.add(new Question("Test Question 3", "A1", "A2", "A3", "A4", 'C'));
        return questions;
    }
    
    private static TestKlass buildTest() {
        return new TestKlass("Test Name", questions(), 60);
    }
    
    @Test
    public void testCreateFromTest() {
        Attempt instance = new Attempt(buildTest(), null);
        assertNotNull("Instantiated", instance);
        assertNull("Student ID not set", instance.getStudentId());
        assertEquals("3 questions cloned", 3, instance.getQuestions().size());
    }
    
    @Test
    public void testCreateFromJSON() {
        Attempt instance = new Attempt(buildTest(), buildJSON(), "12345678");
        assertNotNull("Instantiated", instance);
        assertEquals("Student ID set", "12345678", instance.getStudentId());
        assertEquals("Questions passed", 3, instance.noQuestions());
        assertEquals("Answer set", (Object) 'A', instance.getQuestions().get(0).getSelectedAnswer());
        assertEquals("Answer set", (Object) 'D', instance.getQuestions().get(1).getSelectedAnswer());
        assertNull("Null answer set", instance.getQuestions().get(2).getSelectedAnswer());
    }
    
    /**
     * Mapped question is randomly ordered
     */
    @Test
    public void testGetMappedQuestion() {
        Attempt instance = new Attempt(buildTest(), null);
        assertNotNull("Questions exists", instance.getQuestion(0));
        assertNotNull("Questions exists", instance.getQuestion(1));
        assertNotNull("Questions exists", instance.getQuestion(2));
    }
    
    /**
     * Questions are not mapped when reading already completed attempt
     */
    @Test
    public void testNotMapped() {
        Attempt instance = new Attempt(buildTest(), buildJSON(), "12345678");
        try {
            instance.getQuestion(0);
            fail("Questions are not mapped");
        } catch (IndexOutOfBoundsException ex) {
        }
    }
}
