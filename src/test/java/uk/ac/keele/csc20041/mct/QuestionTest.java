package uk.ac.keele.csc20041.mct;

import java.io.IOException;
import java.io.StringReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author Sam
 */
public class QuestionTest {
    private static final String CSV_TEXT = "Test Question 1,A1,A2,A3,A4,A\r\nTest Question 2,A1,A2,A3,A4,B";
    
    private static Question build() {
        return new Question("Test Question 1", "A1", "A2", "A3", "A4", 'A');
    }

    @Test
    public void testFromParserWithHeader() throws IOException {
        CSVParser parser = CSVFormat.DEFAULT
                .withHeader("question", "a", "b", "c", "d", "answer")
                .parse(new StringReader(CSV_TEXT));
        Question[] qArr = new Question[2];
        int i = 0;
        for (CSVRecord r : parser) {
            qArr[i] = new Question(r);
            i++;
        }
        assertEquals("Question 1 text parsed", "Test Question 1", qArr[0].getQuestion());
        assertEquals("Question 1 answers parsed", "A1", qArr[0].getAnswerA());
        assertEquals("Question 1 answers parsed", "A2", qArr[0].getAnswerB());
        assertEquals("Question 1 answers parsed", "A3", qArr[0].getAnswerC());
        assertEquals("Question 1 answers parsed", "A4", qArr[0].getAnswerD());
        assertEquals("Question 1 answer parsed", 'A', qArr[0].getCorrectAnswer());
        assertEquals("Question 2 text parsed", "Test Question 2", qArr[1].getQuestion());
        assertEquals("Question 2 answers parsed", "A1", qArr[1].getAnswerA());
        assertEquals("Question 2 answers parsed", "A2", qArr[1].getAnswerB());
        assertEquals("Question 2 answers parsed", "A3", qArr[1].getAnswerC());
        assertEquals("Question 2 answers parsed", "A4", qArr[1].getAnswerD());
        assertEquals("Question 2 answer parsed", 'B', qArr[1].getCorrectAnswer());
    }
    
    @Test
    public void testFromParserWithoutHeader() throws IOException {
        CSVParser parser = CSVFormat.DEFAULT.parse(new StringReader(CSV_TEXT));
        Question[] qArr = new Question[2];
        int i = 0;
        for (CSVRecord r : parser) {
            qArr[i] = new Question(r);
            i++;
        }
        assertEquals("Question 1 text parsed", "Test Question 1", qArr[0].getQuestion());
        assertEquals("Question 1 answers parsed", "A1", qArr[0].getAnswerA());
        assertEquals("Question 1 answers parsed", "A2", qArr[0].getAnswerB());
        assertEquals("Question 1 answers parsed", "A3", qArr[0].getAnswerC());
        assertEquals("Question 1 answers parsed", "A4", qArr[0].getAnswerD());
        assertEquals("Question 1 answer parsed", 'A', qArr[0].getCorrectAnswer());
        assertEquals("Question 2 text parsed", "Test Question 2", qArr[1].getQuestion());
        assertEquals("Question 2 answers parsed", "A1", qArr[1].getAnswerA());
        assertEquals("Question 2 answers parsed", "A2", qArr[1].getAnswerB());
        assertEquals("Question 2 answers parsed", "A3", qArr[1].getAnswerC());
        assertEquals("Question 2 answers parsed", "A4", qArr[1].getAnswerD());
        assertEquals("Question 2 answer parsed", 'B', qArr[1].getCorrectAnswer());
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
