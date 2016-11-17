import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class PiCalculator {

    public static void usageMessage() {
        System.out.println("./PiCalculator #threads #iterations");
        System.exit(-1);
    }


    public static void main(String[] args) {
        long nThreads = 0;
        long nIterations = 0;
        long inside = 0;
        final long iterationsPerThread;
        long[] counters;
        Thread[] threads;

        if (args.length != 2) {
            usageMessage();
        }

        try {
            nThreads = Long.parseLong(args[0]);
            nIterations = Long.parseLong(args[1]);
        } catch (NumberFormatException nfe) {
            usageMessage();
        }

        threads = new Thread[(int) nThreads];
        counters = new long[threads.length];
        iterationsPerThread = nIterations / nThreads;

        for (int i = 0; i < threads.length; i++) {
            int id = i;
            threads[i] = new Thread(() -> {
                double x, y;
                ThreadLocalRandom tlr = ThreadLocalRandom.current();
                for (int j = 0; j < iterationsPerThread; j++) {
                    x = tlr.nextDouble(0, 1);
                    y = tlr.nextDouble(0, 1);
                    if (x*x + y*y < 1) {
                        counters[id]++;
                    }
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException ie) {
            }
        }

        for (long c : counters) {
            inside += c;
        }

        double ratio = (double) inside / nIterations;
        double pi = 4 * ratio;



        System.out.printf("Total = %d\n", nIterations);
        System.out.printf("Inside = %d\n", inside);
        System.out.printf("Ratio = %f\n", ratio);
        System.out.printf("Pi = %f\n", pi);

    }

}