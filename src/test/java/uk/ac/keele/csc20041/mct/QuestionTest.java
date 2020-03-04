package uk.ac.keele.csc20041.mct;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * @author Samual
 */
public class QuestionTest {
    private static final String JSON_TEXT = "{\"question\": \"Test Question 1\", \"a\": \"A1\", \"b\": \"A2\", \"c\": \"A3\", \"d\": \"A4\", \"answer\": \"A\"}";

    private static Question build() {
        return new Question("Test Question 1", "A1", "A2", "A3", "A4", 'A');
    }

    @Test
    public void testFromJSON() {
        Question q = new Question((JSONObject) JSONValue.parse(JSON_TEXT));
        assertEquals("Question 1 text parsed", "Test Question 1", q.getQuestion());
        assertEquals("Question 1 answers parsed", "A1", q.getAnswerA());
        assertEquals("Question 1 answers parsed", "A2", q.getAnswerB());
        assertEquals("Question 1 answers parsed", "A3", q.getAnswerC());
        assertEquals("Question 1 answers parsed", "A4", q.getAnswerD());
        assertEquals("Question 1 answer parsed", 'A', q.getCorrectAnswer());
    }

    @Test
    public void testCanSetValidAnswer() {
        Question instance = build();
        assertNull("Starts as null", instance.getSelectedAnswer());
        instance.setAnswer('A');
        assertEquals("Changed to A", (Object) 'A', instance.getSelectedAnswer());
        instance.setAnswer('B');
        assertEquals("Changed to B", (Object) 'B', instance.getSelectedAnswer());
        instance.setAnswer('C');
        assertEquals("Changed to C", (Object) 'C', instance.getSelectedAnswer());
        instance.setAnswer('D');
        assertEquals("Changed to D", (Object) 'D', instance.getSelectedAnswer());
        instance.setAnswer('a');
        assertEquals("Changed to A", (Object) 'A', instance.getSelectedAnswer());
        instance.setAnswer('b');
        assertEquals("Changed to B", (Object) 'B', instance.getSelectedAnswer());
        instance.setAnswer('c');
        assertEquals("Changed to C", (Object) 'C', instance.getSelectedAnswer());
        instance.setAnswer('d');
        assertEquals("Changed to D", (Object) 'D', instance.getSelectedAnswer());
        instance.setAnswer(null);
        assertNull("Changed to null", instance.getSelectedAnswer());
    }

    @Test
    public void testThrowsOnInvalidAnswerSet() {
        Question instance = build();
        try {
            instance.setAnswer(Character.MIN_VALUE);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Exception message thrown", "Invalid answer (\0)", ex.getMessage());
        }
        try {
            instance.setAnswer('@');
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Exception message thrown", "Invalid answer (@)", ex.getMessage());
        }
        try {
            instance.setAnswer('`');
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Exception message thrown", "Invalid answer (`)", ex.getMessage());
        }
        try {
            instance.setAnswer('E');
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Exception message thrown", "Invalid answer (E)", ex.getMessage());
        }
        try {
            instance.setAnswer('e');
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Exception message thrown", "Invalid answer (e)", ex.getMessage());
        }
    }

    @Test
    public void testReportCorrect() {
        Question instance = build();
        assertNull("Starts as null", instance.getSelectedAnswer());
        assertFalse("Null is incorrect", instance.correct());
        instance.setAnswer('B');
        assertFalse("B is incorrect", instance.correct());
        instance.setAnswer('A');
        assertTrue("A is correct", instance.correct());
    }
}
