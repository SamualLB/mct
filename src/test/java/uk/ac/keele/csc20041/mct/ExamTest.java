package uk.ac.keele.csc20041.mct;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * @author sam
 */
public class ExamTest {
    private static final String TEST_JSON = "{\"time_limit\": 60,\"no_questions\": 1,\"questions\": [{\"question\": \"Test Question 1\",\"a\": \"A1\",\"b\": \"A2\",\"c\": \"A3\",\"d\": \"A4\",\"answer\": \"A\"},{\"question\": \"Test Question 2\",\"a\": \"A1\",\"b\": \"A2\",\"c\": \"A3\",\"d\": \"A4\",\"answer\": \"B\"}]}";

    private static Exam build() {
        ArrayList<Question> qs = new ArrayList(2);
        qs.add(new Question("Test Question 1", "A1", "A2", "A3", "A4", 'A'));
        qs.add(new Question("Test Question 2", "A1", "A2", "A3", "A4", 'B'));
        return new Exam(qs, 60, 1);
    }

    @Test
    public void testFromFullFileName() throws IOException {
        File tempFile = File.createTempFile("mct_test", ".mcte");
        FileWriter writer = new FileWriter(tempFile);
        writer.write(TEST_JSON);
        writer.close();
        Exam instance = new Exam(tempFile.getPath());
        assertNotNull("Exam created", instance);
        assertEquals("2 questions", 2, instance.getQuestions().size());
        assertEquals("Time limit parsed", 60, instance.getTimeLimit());
        assertEquals("Number of questions parsed", 1, instance.getNoOfQuestions());
        tempFile.delete();
    }
    
    @Test
    public void testFromMinimalFileName() throws IOException {
        File tempFile = File.createTempFile("mct_test", ".mcte");
        String path = tempFile.getPath();
        String pathWithoutExtension = path.substring(0, path.lastIndexOf('.'));
        FileWriter writer = new FileWriter(tempFile);
        writer.write(TEST_JSON);
        writer.close();
        Exam instance = new Exam(pathWithoutExtension);
        assertNotNull("Exam created", instance);
        assertEquals("2 questions", 2, instance.getQuestions().size());
        assertEquals("Time limit parsed", 60, instance.getTimeLimit());
        assertEquals("Number of questions parsed", 1, instance.getNoOfQuestions());
        tempFile.delete();
    }
    
    @Test
    public void testGeneratesTest() {
        Exam ins = build();
        uk.ac.keele.csc20041.mct.Test test = ins.generateTest("Test Test!");
        assertNotNull("Test Generated", test);
        assertEquals("Name given", "Test Test!", test.getName());
        assertEquals("1 question", 1, test.getQuestions().size());
        assertEquals("Time limit given", 60, test.getTimeLimit());
    }
}
