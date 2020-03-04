/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.keele.csc20041.mct;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Samual
 */
public class TestTest {
    private static ArrayList<Question> questions() {
        ArrayList<Question> questions = new ArrayList(1);
        questions.add(new Question("Test Question 1", "A1", "A2", "A3", "A4", 'A'));
        return questions;
    }
    
    private static Test build() {
        return new Test("Test Name", questions(), 60);
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
