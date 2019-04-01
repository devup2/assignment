package code.one;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.System.err;
import static java.lang.System.out;

public class Problem1 {

    private static final int queueCapacity   =   10000;
    ExecutorService executorService;

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public static void main(String[] args) throws InterruptedException {
        if(args.length < 2){
            err.println("Input and output file required. ");
            out.println("Usage:  ");
            out.println("java code.one.Problem1 src/main/resources/p1_1.txt output.txt");
        }
        String input = args[0];
        String output = args[1];
        Problem1 problem1 = new Problem1();
        problem1.setExecutorService(Executors.newSingleThreadExecutor());
        problem1.process(input, output);
    }

    public void process(String inputFileName, String outputFileName) throws InterruptedException {
        File inputFile = new File(getClass().getClassLoader().getResource( inputFileName).getFile());
        assert inputFile.exists();
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(queueCapacity);
        AtomicBoolean signalCompletion = new AtomicBoolean();
        DataProducer producer = new DataProducer(inputFileName, blockingQueue, signalCompletion);
        DataConsumer consumer = new DataConsumer(outputFileName, blockingQueue, signalCompletion);

        List<Future<Void>> futures = executorService.invokeAll(Arrays.asList(producer, consumer));
        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                err.println("InterruptedException:future.get" + e.getMessage());
            }
        }
        executorService.shutdown();
    }

}
