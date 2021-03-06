package vhdltestbenchgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author Daniele
 */
public class MultithreadedTestBenchGenerationComputation {
    private static final int MAX_THREADS = 8;
    private static final AtomicInteger completionCounter = new AtomicInteger(0);
    private static final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(MAX_THREADS);
    
    private HashMap<Integer, Future<?>> futureMap;
    private final JLabel label;
    private final JProgressBar progressBar;
    
    private int maxTestValue;
    
    public MultithreadedTestBenchGenerationComputation(int maxTestValue, JLabel label, JProgressBar progressBar) {
        this.maxTestValue = maxTestValue;
        futureMap = new HashMap<>();
        this.label = label;
        this.progressBar = progressBar;

    }
    
    
    public void addThread(int startIndex, int endIndex, JLabel label, JProgressBar progressBar) {
        futureMap.put(futureMap.size(), threadPool.submit(compute(startIndex, endIndex)));
    }
    
    private Runnable compute(int startIndex, int endIndex) {
      return () -> {
            
            VHDLTestBenchCreator testBenchCreator = new VHDLTestBenchCreator();
            BufferedWriter writer = null;
            File testBench = null;
            try {
                File directory = new File("Test_Benches");
                directory.mkdir();
                for(int i = startIndex; i <= endIndex; i++) {
                        testBench = new File("Test_Benches\\tb_FSM_" + i + ".vhd");
                        writer = new BufferedWriter(new FileWriter(testBench));
                        writer.write(testBenchCreator.generateTestBench());
                        writer.flush();
                        synchronized(label) {
                            synchronized(progressBar) {
                                progressBar.setValue(completionCounter.incrementAndGet());
                                System.out.println(completionCounter.get() + " : " + (completionCounter.get() / (double)maxTestValue * 100) + " %");
                                label.setText("<html>" + String.format("%.2f", progressBar.getPercentComplete() * 100) + " %  (" + progressBar.getValue() + " / " + 
                                                              progressBar.getMaximum() + ")<br> Generated : " + completionCounter.get() + " / " + maxTestValue + "<html>");
                            }
                        }
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
      };
    }
    
}
