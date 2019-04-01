import code.one.DataConsumer;
import code.one.DataProducer;
import code.one.Problem1;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class Problem1Test {
    Problem1 p1;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Before
    public void setUp() throws Exception {
        p1 = new Problem1();
        p1.setExecutorService( executorService);

    }

    @Test
    public void testLineProducer() throws ExecutionException, InterruptedException {
        String inputFileName    =   "p1_1.txt";
        BlockingQueue<String> queue =   new ArrayBlockingQueue<>(100);
        AtomicBoolean signalCompletion  =   new AtomicBoolean();
        DataProducer producer = new DataProducer(inputFileName, queue, signalCompletion);
        Future<Void> futures = executorService.submit(producer);
        futures.get();
        Collection<String> result =   new ArrayList<>();
        queue.drainTo(result);
        result.stream().forEach(System.out::println);
        Assert.assertEquals("Size", 14, result.size());
        Assert.assertEquals("First element is 'RFG485YH'", "RFG485YH",  ((ArrayList<String>) result).get(0)  );
        executorService.shutdown();
    }

    @Test
    public void testDataConsumer() throws InterruptedException, IOException, ExecutionException {
        BlockingQueue<String> queue =   new ArrayBlockingQueue<>(100);
        Path file = Files.createTempFile( "problem1", "test");
        file.toFile().deleteOnExit();
        List<String> input = Arrays.asList("RRRRRRR3", "4", "5", "RRRRRRR2", "546", "66", "RRRRRRR1", "9999.99");
        List<String> expectedResult = Arrays.asList("RRRRRRR3:5", "RRRRRRR2:66", "RRRRRRR1:9999.99");
        AtomicBoolean signalCompletion = new AtomicBoolean();

        DataConsumer consumer = new DataConsumer(file.toString(), queue, signalCompletion);
        Future<Void> future = executorService.submit(consumer);

        Stream.Builder<String> builder = Stream.builder();
        input.stream().forEach(builder::add);
        builder.build().forEach( queue::offer);
        signalCompletion.set(true);
        future.get();
        executorService.shutdown();
        List<String> actualResult = Files.readAllLines(file);
        Assert.assertTrue("Input matches output", actualResult.containsAll(expectedResult));
    }



    @Test
    public void testProcess() throws IOException, InterruptedException {
        String inputFileName    =   "p1_1.txt";
        Path outputFilePath = Files.createTempFile( "problem1", "test");
        outputFilePath.toFile().deleteOnExit();
        System.out.println("Outfile:" + outputFilePath.toString());


        p1.process(inputFileName, outputFilePath.toString());

    }
}
