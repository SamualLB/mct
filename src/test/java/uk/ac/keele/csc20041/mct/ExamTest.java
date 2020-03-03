package uk.ac.keele.csc20041.mct;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * @author sam
 */
public class ExamTest {
    private static final String QUESTIONS_CSV = "Test Question 1,A1,A2,A3,A4,A\r\nTest Question 2,A1,A2,A3,A4,B";
    
    @Test
    public void testFromFileName() throws IOException {
        File tempFile = File.createTempFile("mct_test", null);
        FileWriter writer = new FileWriter(tempFile);
        writer.write(QUESTIONS_CSV);
        writer.close();
        Exam instance = new Exam(tempFile.getPath());
        assertNotNull("Exam created", instance);
        assertEquals("2 questions", 2, instance.getQuestions().size());
        tempFile.delete();
    }
}
