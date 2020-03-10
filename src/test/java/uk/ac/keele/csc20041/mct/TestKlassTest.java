/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.keele.csc20041.mct;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Samual
 */
public class TestKlassTest {
    private static final String TEST_JSON = "{\"passcode\": \"123456\", \"time_limit\": 60,\"questions\": [{\"question\": \"Test Question 1\",\"a\": \"A1\",\"b\": \"A2\",\"c\": \"A3\",\"d\": \"A4\",\"answer\": \"A\"},{\"question\": \"Test Question 2\",\"a\": \"A1\",\"b\": \"A2\",\"c\": \"A3\",\"d\": \"A4\",\"answer\": \"B\"}], \"attempts\":{}}";

    private static ArrayList<Question> questions() {
        ArrayList<Question> questions = new ArrayList(1);
        questions.add(new Question("Test Question 1", "A1", "A2", "A3", "A4", 'A'));
        return questions;
    }

    private static TestKlass build() {
        return new TestKlass("Test Name", questions(), 60);
    }

    /**
     * From file name with extension
     *
     * @throws IOException Error reading/writing to temp file
     */
    @Test
    public void testFromFullFileName() throws IOException {
        File tempFile = File.createTempFile("mct_test", TestKlass.FILE_EXTENSION);
        FileWriter writer = new FileWriter(tempFile);
        writer.write(TEST_JSON);
        writer.close();
        TestKlass instance = new TestKlass(tempFile.getPath());
        assertNotNull("Test created", instance);
        assertEquals("Time limit parsed", 60, instance.getTimeLimit());
        assertEquals("Passcode parsed", "123456", instance.getPasscode());
        assertEquals("2 questions parsed", 2, instance.getQuestions().size());
        String path = tempFile.getPath();
        assertEquals("Name parsed", path.substring(0, path.lastIndexOf('.')), instance.getName());
        tempFile.delete();
    }
    
    /**
     * From file name without extension
     *
     * @throws IOException Error reading/writing to temp file
     */
    @Test
    public void testFromMinimalFileName() throws IOException {
        File tempFile = File.createTempFile("mct_test", TestKlass.FILE_EXTENSION);
        String path = tempFile.getPath();
        String pathWithoutExtension = path.substring(0, path.lastIndexOf('.'));
        FileWriter writer = new FileWriter(tempFile);
        writer.write(TEST_JSON);
        writer.close();
        TestKlass instance = new TestKlass(pathWithoutExtension);
        assertNotNull("Test created", instance);
        assertEquals("Time limit parsed", 60, instance.getTimeLimit());
        assertEquals("Passcode parsed", "123456", instance.getPasscode());
        assertEquals("2 questions parsed", 2, instance.getQuestions().size());
        assertEquals("Name parsed", pathWithoutExtension, instance.getName());
        tempFile.delete();
    }

    @Test
    public void testCreate() {
        TestKlass instance = build();
        assertNotNull("Not null", instance);
        assertEquals("Name passed", "Test Name", instance.getName());
        assertEquals("Question passed", 1, instance.getQuestions().size());
        assertNotNull("Passcode exists", instance.getPasscode());
    }

    @Test
    public void testPasscode6Digits() {
        TestKlass instance = build();
        assertTrue("Regex check", instance.getPasscode().matches("\\d{6}"));
    }
}
