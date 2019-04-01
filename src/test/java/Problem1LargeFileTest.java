import code.one.Problem1;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Problem1LargeFileTest {
    Problem1 p1;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    String inputFile;

    @Before
    public void setUp() throws Exception {
        p1 = new Problem1();
        p1.setExecutorService( executorService);
        Path inputFilePath = Files.createTempFile("problem1", "large");
        inputFilePath.toFile().deleteOnExit();
        inputFile = inputFilePath.toString();
    }

    @Test
    public void testProcessWithLargeFIle() {

    }
}
