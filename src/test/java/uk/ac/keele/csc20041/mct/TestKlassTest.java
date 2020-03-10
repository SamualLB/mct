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

/**
 *
 * @author Samual
 */
public class TestKlassTest {
    private static final String TEST_JSON = "";
    
    private static ArrayList<Question> questions() {
        ArrayList<Question> questions = new ArrayList(1);
        questions.add(new Question("Test Question 1", "A1", "A2", "A3", "A4", 'A'));
        return questions;
    }
    
    private static Test build() {
        return new Test("Test Name", questions(), 60);
    }
    
    //Test for file name with extension
    @org.junit.Test
    public void testFromFullFileName() throws IOException {
        
        File tempFile = File.createTempFile("mct_test", ".mctt");
        FileWriter writer = new FileWriter(tempFile);
        writer.write(TEST_JSON);
        writer.close();
        TestKlass instance = new TestKlass(tempFile.getPath());
        assertNotNull("Test created", instance);
        assertEquals("Time limit parsed", 60, instance.getTimeLimit());
        tempFile.delete();
        
    }
    
    //Test for file name without extension
    @org.junit.Test
    public void testFromMinimalFileName() throws IOException {
        File tempFile = File.createTempFile("mct_test", ".mctt");
        String path = tempFile.getPath();
        String pathWithoutExtension = path.substring(0, path.lastIndexOf('.'));
        FileWriter writer = new FileWriter(tempFile);
        writer.write(TEST_JSON);
        writer.close();
        TestKlass instance = new TestKlass(pathWithoutExtension);
        assertNotNull("Test created", instance);
        assertEquals("Time limit parsed", 60, instance.getTimeLimit());
        tempFile.delete();
        
    }
    
    
    
    @org.junit.Test
    public void testCreate() {
        Test instance = build();
        assertNotNull("Not null", instance);
        assertEquals("Name passed", "Test Name", instance.getName());
        assertEquals("Question passed", 1, instance.getQuestions().size());
        assertNotNull("Passcode exists", instance.getPasscode());
    }
    
    @org.junit.Test
    public void testPasscode6Digits() {
        Test instance = build();
        assertTrue("Regex check", instance.getPasscode().matches("\\d{6}"));
    }
}
