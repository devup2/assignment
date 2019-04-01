package code.one;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class DataConsumer implements Callable<Void> {
    String output;
    BlockingQueue<String> queue;
    AtomicBoolean signalCompletion;
    private static final String CUSIP_PATTERN = "^[0-9a-zA-Z]{8}$";
    private static final String PRICE_PATTERN = "[0-9\\.]*$";
    Pattern cusipPattern = Pattern.compile(CUSIP_PATTERN);
    Pattern pricePattern = Pattern.compile( PRICE_PATTERN);

    public DataConsumer(String outputFileName, BlockingQueue<String> blockingQueue, AtomicBoolean signalCompletion) {
        this.output =   outputFileName;
        this.queue  =   blockingQueue;
        this.signalCompletion = signalCompletion;
    }

    @Override
    public Void call() throws Exception {
        File outFile = new File(output);
        outFile.delete();

        if(!outFile.createNewFile()){
            return null;
        }
        System.out.println("Created file:" + outFile.toString());
        try (FileWriter fileWriter = new FileWriter(outFile, true);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            String cusip = null;
            String price = null;
            while (!signalCompletion.get() || !queue.isEmpty() ) {
                String line = queue.poll(300, TimeUnit.MILLISECONDS);
                if(isNotEmpty(line)) {
                    if (cusipPattern.matcher(line).matches()) {
                        if (isNotEmpty(cusip)) {
                            writer.write(String.format("%s:%s", cusip, price));
                            writer.newLine();
                            price = null;
                        }
                        cusip = line;
                    } else if (pricePattern.matcher(line).matches()) {
                        price =  line ;
                    }
                }
            }
            if(isNotEmpty(cusip)){
                writer.write(String.format("%s:%s", cusip, price));
                writer.newLine();
            }
        }
        return null;
    }

    private boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}
