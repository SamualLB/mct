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
    private static final String TEST_JSON = "{\"time_limit\": 60,\"no_questions\": 1,\"questions\": [{\"question\": \"Test Question 1\",\"a\": \"A1\",\"b\": \"A2\",\"c\": \"A3\",\"d\": \"A4\",\"answer\": \"A\"},{\"question\": \"Test Question 2\",\"a\": \"A1\",\"b\": \"A2\",\"c\": \"A3\",\"d\": \"A4\",\"answer\": \"B\"}]}";

    @Test
    public void testFromFileName() throws IOException {
        File tempFile = File.createTempFile("mct_test", null);
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
}
