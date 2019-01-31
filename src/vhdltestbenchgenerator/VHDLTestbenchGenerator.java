package vhdltestbenchgenerator;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author Daniele
 */
public class VHDLTestbenchGenerator {

    private static final int maxTestValue = 1000;
    private static final int maxScheduledThreadPool = 8;
    private static MultithreadedTestBenchGenerationComputation multithreadedTestBenchCreator;
    
    private static JProgressBar progressBar;
    private static JLabel label;
    
    
    public static void main(String[] args) {
        
        VHDLTestBenchCreator testBenchCreator = new VHDLTestBenchCreator();
        JFrame frame = new JFrame("Test benches generated");
        frame.setPreferredSize(new Dimension(300, 100));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        JPanel panel = new JPanel(new FlowLayout());
        progressBar = new JProgressBar(0, maxTestValue);
        label = new JLabel(progressBar.getPercentComplete() *100 + " %  (" + progressBar.getValue() + " / " + progressBar.getMaximum() + ")");
        panel.add(progressBar);
        panel.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        multithreadedTestBenchCreator = new MultithreadedTestBenchGenerationComputation(maxTestValue, label, progressBar);
        for(int i = 0; i < maxScheduledThreadPool; i++)
            multithreadedTestBenchCreator.addThread(i / maxScheduledThreadPool * maxTestValue, (i + 1) / maxScheduledThreadPool * maxTestValue - 1, label, progressBar);
    }
    
}
