package code.one;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.System.*;

public class DataProducer implements Callable<Void> {
    private final AtomicBoolean signalCompletion;
    String source;
    BlockingQueue<String> queue;
    public DataProducer(String inputFile, BlockingQueue<String> queue, AtomicBoolean signalCompletion) {
        source  =   inputFile;
        this.queue  =   queue;
        this.signalCompletion = signalCompletion;
    }

    private static boolean filterOutCommentAndEmptyLines(String s) {
        return !s.matches("^.{0}$") && !s.matches("[^0-9a-zA-Z]");
    }

    @Override
    public Void call() throws Exception {
        try {
            Path path = Paths.get(getClass().getClassLoader()
                    .getResource(source).toURI());
            try(    BufferedReader reader = Files.newBufferedReader( path) ) {
                reader.lines()
                        .filter(DataProducer::filterOutCommentAndEmptyLines)
                        .forEach( queue::offer);
            } catch (IOException ioe) {
                err.println("FILE_READ_EXCEPTION:" + source +  ioe.getMessage());
            }
        } catch (URISyntaxException e) {
            err.println("FILE_NOT_FOUND:" + source +  e.getMessage());
        }finally {
            signalCompletion.lazySet( true);
        }
        return null;
    }
}
